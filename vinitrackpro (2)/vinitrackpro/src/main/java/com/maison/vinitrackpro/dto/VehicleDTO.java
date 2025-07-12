package com.maison.vinitrackpro.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.maison.vinitrackpro.model.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {

    private Long id;
    private String registrationNumber;
    private String make;
    private String model;
    private VehicleType type;
    private double capacity;
    private LocalDate lastMaintenanceDate;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
}
