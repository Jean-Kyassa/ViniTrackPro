package com.maison.vinitrackpro.dto;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maison.vinitrackpro.model.OrderStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
public class DeliveryDTO {

     public static class DeliveryOrderDTO {
        private Long id;
        
        @NotBlank(message = "Order number is required")
        private String orderNumber;
        
        @NotNull(message = "Customer ID is required")
        private Long customerId;
        private String companyName;
        
        @NotBlank(message = "Delivery address is required")
        private String deliveryAddress;
        
        private OrderStatus status;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime orderDate;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deliveryDate;
        
        private Long routeId;
        private String routeName;
        
        private List<OrderItemDTO> items;
        
        private String deliveryInstructions;
        
        @Positive(message = "Delivery fee must be positive")
        private double deliveryFee;
        
        @Positive(message = "Total amount must be positive")
        private double totalAmount;
        
        private Long createdBy;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
        
        // Constructors
        public DeliveryOrderDTO() {}
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getOrderNumber() { return orderNumber; }
        public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
        
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        
        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }
        
        public String getDeliveryAddress() { return deliveryAddress; }
        public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
        
        public OrderStatus getStatus() { return status; }
        public void setStatus(OrderStatus status) { this.status = status; }
        
        public LocalDateTime getOrderDate() { return orderDate; }
        public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
        
        public LocalDateTime getDeliveryDate() { return deliveryDate; }
        public void setDeliveryDate(LocalDateTime deliveryDate) { this.deliveryDate = deliveryDate; }
        
        public Long getRouteId() { return routeId; }
        public void setRouteId(Long routeId) { this.routeId = routeId; }
        
        public String getRouteName() { return routeName; }
        public void setRouteName(String routeName) { this.routeName = routeName; }
        
        public List<OrderItemDTO> getItems() { return items; }
        public void setItems(List<OrderItemDTO> items) { this.items = items; }
        
        public String getDeliveryInstructions() { return deliveryInstructions; }
        public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }
        
        public double getDeliveryFee() { return deliveryFee; }
        public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }
        
        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
        
        public Long getCreatedBy() { return createdBy; }
        public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
        
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

//     @Override
// public String toString() {
//     return "DeliveryDTO{" +
//            "deliveryOrder=" + new DeliveryOrderDTO().toString() +
//            '}';
// }
    public static class OrderItemDTO {
        private Long id;
        private Long itemId;
        private String itemName;
        private int quantity;
        private double unitPrice;
        private double totalPrice;
        
        // Constructors
        public OrderItemDTO() {}
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }
        
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
        
        public double getTotalPrice() { return totalPrice; }
        public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    }
    
    public static class DeliveryRouteDTO {
        private Long id;
        private String name;
        private Long driverId;
        private String driverName;
        private Long vehicleId;
        private String vehicleRegistration;
        private String date;
        private double totalDistance;
        private double estimatedDuration;
        private int totalStops;
        private double totalLoad;
        private List<DeliveryOrderDTO> orders;
        
        // Constructors
        public DeliveryRouteDTO() {}
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Long getDriverId() { return driverId; }
        public void setDriverId(Long driverId) { this.driverId = driverId; }
        
        public String getDriverName() { return driverName; }
        public void setDriverName(String driverName) { this.driverName = driverName; }
        
        public Long getVehicleId() { return vehicleId; }
        public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
        
        public String getVehicleRegistration() { return vehicleRegistration; }
        public void setVehicleRegistration(String vehicleRegistration) { this.vehicleRegistration = vehicleRegistration; }
        
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        
        public double getTotalDistance() { return totalDistance; }
        public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
        
        public double getEstimatedDuration() { return estimatedDuration; }
        public void setEstimatedDuration(double estimatedDuration) { this.estimatedDuration = estimatedDuration; }
        
        public int getTotalStops() { return totalStops; }
        public void setTotalStops(int totalStops) { this.totalStops = totalStops; }
        
        public double getTotalLoad() { return totalLoad; }
        public void setTotalLoad(double totalLoad) { this.totalLoad = totalLoad; }
        
        public List<DeliveryOrderDTO> getOrders() { return orders; }
        public void setOrders(List<DeliveryOrderDTO> orders) { this.orders = orders; }
    }
}
