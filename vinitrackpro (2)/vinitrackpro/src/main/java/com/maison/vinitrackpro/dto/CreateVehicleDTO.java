package com.maison.vinitrackpro.dto;
import java.time.LocalDate;

import com.maison.vinitrackpro.model.VehicleType;

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
public class CreateVehicleDTO {

     @NotBlank(message = "Registration number is required")
    private String registrationNumber;
    
    @NotBlank(message = "Make is required")
    private String make;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Vehicle type is required")
    private VehicleType type;
    
    @Positive(message = "Capacity must be positive")
    private double capacity;
    
    private LocalDate lastMaintenanceDate;
}
