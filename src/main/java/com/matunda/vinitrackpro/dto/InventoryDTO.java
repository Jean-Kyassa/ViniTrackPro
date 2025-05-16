package com.matunda.vinitrackpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {

    private Long id;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productName;
    
    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    private Long quantity;
    
    private Integer minimumStockLevel; // Ensure no invalid characters or syntax issues
    
    private Long maximumStockLevel;
    
    private String location;
    
    private String batchNumber;
    
    private LocalDateTime expiryDate;
    
    private String status;

    private Long updatedById;
    
    private String notes;
    
    private LocalDateTime lastStockUpdate;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}