package com.matunda.vinitrackpro.dto;
import com.matunda.vinitrackpro.model.Product.ProductType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Product SKU is required")
    @Size(min = 2, max = 50, message = "Product SKU must be between 2 and 50 characters")
    private String sku;
    
    @NotBlank(message = "Product type is required")
    @Size(min = 2, max = 50, message = "Product code must be between 2 and 50 characters")
    private ProductType type;
    
    private String description;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @NotNull(message = "Category is required")
    private Long categoryId;
    
    private String categoryName;
    
    private Long supplierId;
    
    private String supplierName;
    
    private String vintage;
    
    private String varietal;
    
    private String region;
    
    private String alcoholContent;
    
    private String imageUrl;

    //private Long categoryId;
   // private String categoryName;
    
    private boolean active;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
