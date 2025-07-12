package com.maison.vinitrackpro.dto;
import java.time.LocalDateTime;

import com.maison.vinitrackpro.model.LineStatus;
import com.maison.vinitrackpro.model.LineType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class CreateProductionLineDTO {

    @NotBlank(message = "Production line name is required")
    private String name;
    
    @NotNull(message = "Line type is required")
    private LineType type;
    
    @Min(value = 0, message = "Efficiency cannot be negative")
    @Max(value = 100, message = "Efficiency cannot exceed 100%")
    private double efficiency;
    
    private LocalDateTime lastMaintenance;
    
    @NotNull(message = "Line status is required")
    private LineStatus status;

}
