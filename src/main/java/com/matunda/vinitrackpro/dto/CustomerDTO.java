package com.matunda.vinitrackpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long id;
    
    @NotBlank(message = "Customer name is required")
    private String name;
    
    @NotBlank(message = "Contact person is required")
    private String contactPerson;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Phone number is required")
    private String phone;
    
    private String address;
    
    private String city;
    
    private String state;
    
    private String country;
    
    private String postalCode;
    
    private String customerType;
    
    private String taxId;
    
    private String notes;
    
    private boolean active;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
