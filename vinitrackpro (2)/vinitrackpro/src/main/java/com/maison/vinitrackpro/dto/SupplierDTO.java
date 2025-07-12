package com.maison.vinitrackpro.dto;
import com.maison.vinitrackpro.model.Currency;
import com.maison.vinitrackpro.model.PaymentTerms;
import com.maison.vinitrackpro.model.ProductCategory;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDTO {

    private Long id;
    private String companyName;
    private String contactPerson;
    private String email;
    private String phone;
    private String businessAddress;
    private ProductCategory productCategories;
    private int leadTime;
    private int minimumOrderQuantity;
    private String taxId;
    private PaymentTerms paymentTerms;
    private Currency currency;
    
    // Getters, setters
}
