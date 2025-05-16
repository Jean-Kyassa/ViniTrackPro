package com.matunda.vinitrackpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    private long totalProducts;
    private long totalCustomers;
    private long totalOrders;
    private long totalInventoryItems;
    private long pendingDeliveriesCount;
    private long pendingQualityControlsCount;

    // Order metrics
    private List<OrderDTO> pendingOrders;
    private int pendingOrdersCount;

    // Inventory metrics
    private List<InventoryDTO> lowStockItems;
    private int lowStockItemsCount;
    private List<InventoryDTO> expiringItems;
    private int expiringItemsCount;

    // Revenue metrics
    private BigDecimal currentMonthRevenue;
    private BigDecimal previousMonthRevenue;
    private BigDecimal revenueGrowthPercentage;
}
