package com.maison.vinitrackpro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

     private Long productId;
    private String name;
    private String code;
    private String description;
    private String category;
    private BigDecimal price;
    private String unit;
    private LocalDateTime createdAt;
}
