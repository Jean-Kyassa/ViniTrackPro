package com.matunda.vinitrackpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;
    
    private String orderNumber;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private String customerName;
    
    private LocalDateTime orderDate;

    private LocalDateTime deliveryDate;
    
    private String status;
    
    private BigDecimal totalAmount;

    private LocalDateTime CancellationDate;
    
    private BigDecimal taxAmount;
    
    private BigDecimal shippingCost;
    
    private BigDecimal discountAmount;
    
    private BigDecimal finalAmount;
    
    private String paymentStatus;
    
    private String shippingAddress;
    
    private String billingAddress;
    
    private String notes;
    
    private Long userId;
    
    private String userName;
    
    private List<OrderItemDTO> orderItems;
    
    private LocalDateTime createdAt;

    private String assignedTo;
    
    private LocalDateTime updatedAt;
}