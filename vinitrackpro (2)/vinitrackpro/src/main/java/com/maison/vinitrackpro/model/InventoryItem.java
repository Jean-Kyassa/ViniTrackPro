package com.maison.vinitrackpro.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String itemName;
    
    @NotBlank
    private String sku;
    
    @Enumerated(EnumType.STRING)
    private ItemCategory category;
    
    @Enumerated(EnumType.STRING)
    private UnitType unitType;
    
    private int initialQuantity;
    private int currentQuantity;
    private int minimumStockLevel;
    
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    
    private double costPrice;
    private double sellingPrice;
    
    @Column(length = 2000)
    private String description;
    
    private String storageLocation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    private LocalDateTime createdAt;
    
    // Getters, setters, constructors
}