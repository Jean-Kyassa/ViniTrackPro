package com.maison.vinitrackpro.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.maison.vinitrackpro.dto.DeliveryDTO.DeliveryOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryRouteDTO {

    private Long id;
    private String name;
    private Long driverId;
    private String driverName;
    private Long vehicleId;
    private String vehicleRegistrationNumber;
    private LocalDate date;
    private List<DeliveryOrderDTO> orders;
    private double totalDistance;
    private double estimatedDuration;
    private int totalStops;
    private double totalLoad;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
}
