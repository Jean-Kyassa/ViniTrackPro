package com.matunda.vinitrackpro.service;

import com.matunda.vinitrackpro.dto.DashboardDTO;
import com.matunda.vinitrackpro.dto.InventoryDTO;
import com.matunda.vinitrackpro.dto.OrderDTO;
import com.matunda.vinitrackpro.repository.*;
import com.matunda.vinitrackpro.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final CustomerRepository customerRepository;
    private final DeliveryRepository deliveryRepository;
    private final QualityControlRepository qualityControlRepository;
    private final InventoryService inventoryService;
    private final OrderService orderService;

    public DashboardDTO getDashboardData() {
        DashboardDTO dashboard = new DashboardDTO();

        // Get basic counts
        dashboard.setTotalProducts(productRepository.count());
        dashboard.setTotalCustomers(customerRepository.count());
        dashboard.setTotalOrders(orderRepository.count());
        dashboard.setTotalInventoryItems(inventoryRepository.count());

        // Get pending orders
        List<OrderDTO> pendingOrders = orderService.getOrdersByStatus(Order.OrderStatus.PENDING.name());
        dashboard.setPendingOrders(pendingOrders);
        dashboard.setPendingOrdersCount(pendingOrders.size());

        // Get low stock items
        List<InventoryDTO> lowStockItems = inventoryService.getLowStockItems();
        dashboard.setLowStockItems(lowStockItems);
        dashboard.setLowStockItemsCount(lowStockItems.size());

        // Get expiring inventory (items expiring in the next 30 days)
        List<InventoryDTO> expiringItems = inventoryService.getExpiringItems(30);
        dashboard.setExpiringItems(expiringItems);
        dashboard.setExpiringItemsCount(expiringItems.size());

        // Get pending deliveries
        dashboard.setPendingDeliveriesCount(
                deliveryRepository.findByStatus(Delivery.DeliveryStatus.SCHEDULED).size()
        );

        // Get pending quality controls
        dashboard.setPendingQualityControlsCount(
                qualityControlRepository.findByStatus(QualityControl.QCStatus.PENDING).size()
        );

        // Calculate revenue metrics
        calculateRevenueMetrics(dashboard);

        return dashboard;
    }

    private void calculateRevenueMetrics(DashboardDTO dashboard) {
        // Current month revenue
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1).atStartOfDay().minusSeconds(1);

        BigDecimal monthlyRevenue = orderRepository
                .findByStatusAndDeliveryDateBetween(Order.OrderStatus.DELIVERED, startOfMonth, endOfMonth)
                .stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dashboard.setCurrentMonthRevenue(monthlyRevenue);

        // Previous month revenue
        LocalDateTime startOfPrevMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfPrevMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay().minusSeconds(1);

        BigDecimal prevMonthRevenue = orderRepository
                .findByStatusAndDeliveryDateBetween(Order.OrderStatus.DELIVERED, startOfPrevMonth, endOfPrevMonth)
                .stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dashboard.setPreviousMonthRevenue(prevMonthRevenue);

        // Calculate revenue growth percentage
        if (prevMonthRevenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal growth = monthlyRevenue.subtract(prevMonthRevenue)
                    .divide(prevMonthRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            dashboard.setRevenueGrowthPercentage(growth);
        } else {
            dashboard.setRevenueGrowthPercentage(BigDecimal.ZERO);
        }
    }
}