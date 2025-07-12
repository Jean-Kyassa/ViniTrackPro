package com.maison.vinitrackpro.dto;

import com.maison.vinitrackpro.model.PaymentTerms;
import lombok.AllArgsConstructor;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    private Long id;
    private String companyName;
    private String contactPerson;
    private String email;
    private String phone;
    private String billingAddress;
    private String shippingAddress;
    private PaymentTerms paymentTerms;
    private String taxId;
    
    // Getters, setters
}
