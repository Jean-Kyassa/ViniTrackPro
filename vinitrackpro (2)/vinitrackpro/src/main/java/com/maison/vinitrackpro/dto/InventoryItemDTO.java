package com.maison.vinitrackpro.dto;
import java.time.LocalDate;

import com.maison.vinitrackpro.model.ItemCategory;
import com.maison.vinitrackpro.model.UnitType;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemDTO {

    private Long id;
    private String itemName;
    private String sku;
    private ItemCategory category;
    private UnitType unitType;
    private int initialQuantity;
    private int currentQuantity;
    private int minimumStockLevel;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private double costPrice;
    private double sellingPrice;
    private String description;
    private String storageLocation;
    
    // Getters, setters
}
