package com.maison.vinitrackpro.dto;
import java.time.LocalDateTime;
import java.util.List;

import com.maison.vinitrackpro.model.ScheduleStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionScheduleDTO {

    private Long id;
    private String name;
    private Long productionLineId;
    private String productionLineName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ScheduleStatus status;
    private Long createdById;
    private String createdByUsername;
    private LocalDateTime createdAt;
    private List<ProductionTaskDTO> tasks;
    
    // Getters and setters
    // Constructors (default and with fields
}

