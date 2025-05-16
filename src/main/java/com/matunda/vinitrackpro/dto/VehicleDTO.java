package com.matunda.vinitrackpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {

    private Long id;
    
    @NotBlank(message = "Registration number is required")
    private String registrationNumber;
    
    private String vehicleType;
    
    private String make;
    
    private String model;
    
    private Integer year;
    
    private String capacity;
    
    private String status;
    
    private LocalDateTime lastMaintenanceDate;
    
    private LocalDateTime nextMaintenanceDate;
    
    private String notes;
    
    private boolean active;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
