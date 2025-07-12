package com.maison.vinitrackpro.model;

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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "suppliers")
public class Supplier {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String companyName;
    
    @NotBlank
    private String contactPerson;
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String phone;
    
    @NotBlank
    @Column(length = 1000)
    private String businessAddress;
    
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategories;
    
    private int leadTime; // in days
    private int minimumOrderQuantity;
    
    @NotBlank
    private String taxId;
    
    @Enumerated(EnumType.STRING)
    private PaymentTerms paymentTerms;
    
    @Enumerated(EnumType.STRING)
    private Currency currency;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    private LocalDateTime createdAt;
    
    // Getters, setters, constructors
}
