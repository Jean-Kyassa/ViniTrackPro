package com.maison.vinitrackpro.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreateProductionBatchDTO {

     @NotBlank(message = "Batch number is required")
    private String batchNumber;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @Positive(message = "Quantity must be positive")
    private int quantity;
    
    @NotNull(message = "Production date is required")
    private LocalDate productionDate;
    
    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;
    @NotNull(message = "User ID is required")
    private Long userId;
    
}
