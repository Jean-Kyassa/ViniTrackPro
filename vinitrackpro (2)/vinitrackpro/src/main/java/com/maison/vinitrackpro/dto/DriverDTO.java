package com.maison.vinitrackpro.dto;
import java.time.LocalDateTime;

import com.maison.vinitrackpro.model.DriverStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDTO {

    private Long id;
    private String name;
    private String licenseNumber;
    private String phone;
    private String email;
    private DriverStatus status;
    private double rating;
    private Long vehicleId;
    private String vehicleRegistrationNumber;
    private Long currentRouteId;
    private String currentRouteName;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
}
