package com.maison.vinitrackpro.dto;

import com.maison.vinitrackpro.model.DriverStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDriverDTO {

    @NotBlank(message = "Driver name is required")
    private String name;
    
    @NotBlank(message = "License number is required")
    private String licenseNumber;
    
    @NotBlank(message = "Phone number is required")
    private String phone;
    
    @Email(message = "Valid email is required")
    private String email;
    
    @NotNull(message = "Driver status is required")
    private DriverStatus status;
    
    private double rating;
    private Long vehicleId;
}
