package com.matunda.vinitrackpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long id;
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productName;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Long quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal totalPrice;

    private BigDecimal taxAmount;

    private String productSku; // Added field for SKU
    
    private BigDecimal discountAmount;
    
    private String notes;

    private BigDecimal subtotal;
}
