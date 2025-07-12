package com.maison.vinitrackpro.dto;
import java.time.LocalDateTime;

import com.maison.vinitrackpro.model.LineStatus;
import com.maison.vinitrackpro.model.LineType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionLineDTO {

    private Long id;
    private String name;
    private LineType type;
    private double efficiency;
    private LocalDateTime lastMaintenance;
    private LineStatus status;
}
