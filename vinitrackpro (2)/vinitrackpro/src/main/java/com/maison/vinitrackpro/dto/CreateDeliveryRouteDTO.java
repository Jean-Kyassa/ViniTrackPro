package com.maison.vinitrackpro.dto;
import java.time.LocalDate;

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
public class CreateDeliveryRouteDTO {

    @NotBlank(message = "Route name is required")
    private String name;
    
    @NotNull(message = "Driver ID is required")
    private Long driverId;
    
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    @Positive(message = "Total distance must be positive")
    private double totalDistance;
    
    @Positive(message = "Estimated duration must be positive")
    private double estimatedDuration;
    
    private int totalStops;
    
    @Positive(message = "Total load must be positive")
    private double totalLoad;
}
