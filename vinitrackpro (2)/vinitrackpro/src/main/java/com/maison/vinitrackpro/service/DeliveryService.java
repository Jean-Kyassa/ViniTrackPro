package com.maison.vinitrackpro.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maison.vinitrackpro.dto.DeliveryDTO;
import com.maison.vinitrackpro.model.Customer;
import com.maison.vinitrackpro.model.DeliveryOrder;
import com.maison.vinitrackpro.model.OrderItem;
import com.maison.vinitrackpro.model.OrderStatus;
import com.maison.vinitrackpro.repository.CustomerRepository;
import com.maison.vinitrackpro.repository.DeliveryRepository;
import com.maison.vinitrackpro.repository.UserRepository;

@Service
@Transactional
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private CustomerRepository customerRepository; // Add this
    
    @Autowired
    private UserRepository userRepository; // Add this if needed
    
    // // Create new delivery order
    // public DeliveryDTO.DeliveryOrderDTO createDeliveryOrder(DeliveryDTO.DeliveryOrderDTO deliveryOrderDTO) {
    //     DeliveryOrder deliveryOrder = convertToEntity(deliveryOrderDTO);
    //     deliveryOrder.setOrderDate(LocalDateTime.now());
    //     deliveryOrder.setCreatedAt(LocalDateTime.now());
    //     deliveryOrder.setStatus(OrderStatus.PENDING);
        
    //     DeliveryOrder savedOrder = deliveryRepository.save(deliveryOrder);
    //     return convertToDTO(savedOrder);
    // }
    // Create new delivery order
    public DeliveryDTO.DeliveryOrderDTO createDeliveryOrder(DeliveryDTO.DeliveryOrderDTO deliveryOrderDTO) {
        DeliveryOrder deliveryOrder = mapToEntity(deliveryOrderDTO);
        deliveryOrder.setOrderDate(LocalDateTime.now());
        deliveryOrder.setCreatedAt(LocalDateTime.now());
        deliveryOrder.setStatus(OrderStatus.PENDING);
        
        // Set customer relationship
        if (deliveryOrderDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findById(deliveryOrderDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + deliveryOrderDTO.getCustomerId()));
            deliveryOrder.setCustomer(customer);
        }
        
        DeliveryOrder savedOrder = deliveryRepository.save(deliveryOrder);
        return convertToDTO(savedOrder);
    }
    // Update the convertToEntity method
    private DeliveryOrder convertToEntity(DeliveryDTO.DeliveryOrderDTO dto) {
        DeliveryOrder order = new DeliveryOrder();
        order.setOrderNumber(dto.getOrderNumber());
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setStatus(dto.getStatus() != null ? dto.getStatus() : OrderStatus.PENDING);
        order.setOrderDate(dto.getOrderDate() != null ? dto.getOrderDate() : LocalDateTime.now());
        order.setDeliveryDate(dto.getDeliveryDate());
        order.setDeliveryInstructions(dto.getDeliveryInstructions());
        order.setDeliveryFee(dto.getDeliveryFee());
        order.setTotalAmount(dto.getTotalAmount());
        
        return order;
    }

    
    // Get delivery order by ID
    public Optional<DeliveryDTO.DeliveryOrderDTO> getDeliveryOrderById(Long id) {
        return deliveryRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    // Get delivery order by order number
    public Optional<DeliveryDTO.DeliveryOrderDTO> getDeliveryOrderByOrderNumber(String orderNumber) {
        return deliveryRepository.findByOrderNumber(orderNumber)
                .map(this::convertToDTO);
    }
    
    // Get all delivery orders
    public List<DeliveryDTO.DeliveryOrderDTO> getAllDeliveryOrders() {
        return deliveryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get delivery orders with pagination
    public Page<DeliveryDTO.DeliveryOrderDTO> getDeliveryOrders(Pageable pageable) {
        return deliveryRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    // Update delivery order
    public DeliveryDTO.DeliveryOrderDTO updateDeliveryOrder(Long id, DeliveryDTO.DeliveryOrderDTO deliveryOrderDTO) {
        Optional<DeliveryOrder> existingOrder = deliveryRepository.findById(id);
        if (existingOrder.isPresent()) {
            DeliveryOrder orderToUpdate = existingOrder.get();
            updateEntityFromDTO(orderToUpdate, deliveryOrderDTO);
            DeliveryOrder updatedOrder = deliveryRepository.save(orderToUpdate);
            return convertToDTO(updatedOrder);
        }
        throw new RuntimeException("Delivery order not found with id: " + id);
    }

     // Update order status
    public DeliveryDTO.DeliveryOrderDTO updateOrderStatus(Long id, OrderStatus status) {
        Optional<DeliveryOrder> existingOrder = deliveryRepository.findById(id);
        if (existingOrder.isPresent()) {
            DeliveryOrder orderToUpdate = existingOrder.get();
            orderToUpdate.setStatus(status);
            
            // Set delivery date when status is DELIVERED
            if (status == OrderStatus.DELIVERED) {
                orderToUpdate.setDeliveryDate(LocalDateTime.now());
            }
            
            DeliveryOrder updatedOrder = deliveryRepository.save(orderToUpdate);
            return convertToDTO(updatedOrder);
        }
        throw new RuntimeException("Delivery order not found with id: " + id);
    }
    
    // Delete delivery order
    public void deleteDeliveryOrder(Long id) {
        if (deliveryRepository.existsById(id)) {
            deliveryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Delivery order not found with id: " + id);
        }
    }
    
    // Get orders by customer
    public List<DeliveryDTO.DeliveryOrderDTO> getOrdersByCustomer(Long customerId) {
        return deliveryRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get orders by status
    public List<DeliveryDTO.DeliveryOrderDTO> getOrdersByStatus(OrderStatus status) {
        return deliveryRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get orders by driver
    public List<DeliveryDTO.DeliveryOrderDTO> getOrdersByDriver(Long driverId) {
        return deliveryRepository.findByDriverId(driverId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get orders by route
    public List<DeliveryDTO.DeliveryOrderDTO> getOrdersByRoute(Long routeId) {
        return deliveryRepository.findByRouteId(routeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get pending orders
    public List<DeliveryDTO.DeliveryOrderDTO> getPendingOrders() {
        return deliveryRepository.findPendingOrders().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Search orders
    public Page<DeliveryDTO.DeliveryOrderDTO> searchOrders(String keyword, Pageable pageable) {
        return deliveryRepository.searchOrders(keyword, pageable)
                .map(this::convertToDTO);
    }
    
    // Get orders by date range
    public List<DeliveryDTO.DeliveryOrderDTO> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return deliveryRepository.findByOrderDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get delivery statistics
    public DeliveryStatistics getDeliveryStatistics() {
        long totalOrders = deliveryRepository.count();
        long pendingOrders = deliveryRepository.countByStatus(OrderStatus.PENDING);
        long processingOrders = deliveryRepository.countByStatus(OrderStatus.PROCESSING);
        long inTransitOrders = deliveryRepository.countByStatus(OrderStatus.IN_TRANSIT);
        long deliveredOrders = deliveryRepository.countByStatus(OrderStatus.DELIVERED);
        long cancelledOrders = deliveryRepository.countByStatus(OrderStatus.CANCELLED);
        
        return new DeliveryStatistics(totalOrders, pendingOrders, processingOrders, 
                                    inTransitOrders, deliveredOrders, cancelledOrders);
    }
    
    // Convert Entity to DTO
    private DeliveryDTO.DeliveryOrderDTO convertToDTO(DeliveryOrder order) {
        DeliveryDTO.DeliveryOrderDTO dto = new DeliveryDTO.DeliveryOrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomerId(order.getCustomer() != null ? order.getCustomer().getId() : null);
        dto.setCompanyName(order.getCustomer() != null ? order.getCustomer().getCompanyName() : null);
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());
        dto.setDeliveryDate(order.getDeliveryDate());
        dto.setRouteId(order.getRoute() != null ? order.getRoute().getId() : null);
        dto.setRouteName(order.getRoute() != null ? order.getRoute().getName() : null);
        dto.setDeliveryInstructions(order.getDeliveryInstructions());
        dto.setDeliveryFee(order.getDeliveryFee());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedBy(order.getCreatedBy() != null ? order.getCreatedBy().getId() : null);
        dto.setCreatedAt(order.getCreatedAt());
        
        // Convert order items
        if (order.getItems() != null) {
            List<DeliveryDTO.OrderItemDTO> itemDTOs = order.getItems().stream()
                    .map(this::convertItemToDTO)
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }
        
        return dto;
    }
    
    // Convert DTO to Entity
    private DeliveryOrder mapToEntity(DeliveryDTO.DeliveryOrderDTO dto) {
        DeliveryOrder order = new DeliveryOrder();
        order.setOrderNumber(dto.getOrderNumber());
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setStatus(dto.getStatus());
        order.setOrderDate(dto.getOrderDate());
        order.setDeliveryDate(dto.getDeliveryDate());
        order.setDeliveryInstructions(dto.getDeliveryInstructions());
        order.setDeliveryFee(dto.getDeliveryFee());
        order.setTotalAmount(dto.getTotalAmount());
        
        // Note: Customer, Route, CreatedBy, and Items would need to be set separately
        // based on the IDs provided in the DTO
        
        return order;
    }
    
    // Update entity from DTO
    private void updateEntityFromDTO(DeliveryOrder order, DeliveryDTO.DeliveryOrderDTO dto) {
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setDeliveryInstructions(dto.getDeliveryInstructions());
        order.setDeliveryFee(dto.getDeliveryFee());
        order.setTotalAmount(dto.getTotalAmount());
        
        if (dto.getStatus() != null) {
            order.setStatus(dto.getStatus());
        }
          // Update customer if provided
          if (dto.getCustomerId() != null && !dto.getCustomerId().equals(order.getCustomer().getId())) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + dto.getCustomerId()));
            order.setCustomer(customer);
        }
    }
    
    // Convert OrderItem to DTO
    private DeliveryDTO.OrderItemDTO convertItemToDTO(OrderItem item) {
        DeliveryDTO.OrderItemDTO dto = new DeliveryDTO.OrderItemDTO();
        dto.setId(item.getId());
        dto.setItemId(item.getItem() != null ? item.getItem().getId() : null);
        dto.setItemName(item.getItem() != null ? item.getItem().getItemName() : null);
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setTotalPrice(item.getQuantity() * item.getUnitPrice());
        return dto;
    }
    
    // Inner class for delivery statistics
    public static class DeliveryStatistics {
        private long totalOrders;
        private long pendingOrders;
        private long processingOrders;
        private long inTransitOrders;
        private long deliveredOrders;
        private long cancelledOrders;
        
        public DeliveryStatistics(long totalOrders, long pendingOrders, long processingOrders,
                                long inTransitOrders, long deliveredOrders, long cancelledOrders) {
            this.totalOrders = totalOrders;
            this.pendingOrders = pendingOrders;
            this.processingOrders = processingOrders;
            this.inTransitOrders = inTransitOrders;
            this.deliveredOrders = deliveredOrders;
            this.cancelledOrders = cancelledOrders;
        }
        
        // Getters
        public long getTotalOrders() { return totalOrders; }
        public long getPendingOrders() { return pendingOrders; }
        public long getProcessingOrders() { return processingOrders; }
        public long getInTransitOrders() { return inTransitOrders; }
        public long getDeliveredOrders() { return deliveredOrders; }
        public long getCancelledOrders() { return cancelledOrders; }
    }
}
