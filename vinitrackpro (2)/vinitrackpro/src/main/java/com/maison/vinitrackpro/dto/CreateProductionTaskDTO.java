package com.maison.vinitrackpro.dto;
import java.time.LocalDateTime;

import com.maison.vinitrackpro.model.TaskStatus;

import jakarta.validation.constraints.Min;
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
public class CreateProductionTaskDTO {

    @NotBlank(message = "Task name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Schedule ID is required")
    private Long scheduleId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @Positive(message = "Quantity must be positive")
    private int quantity;
    
    @Min(value = 1, message = "Priority must be at least 1")
    private int priority;
    
    @NotNull(message = "Task status is required")
    private TaskStatus status;
    
    private LocalDateTime estimatedStart;
    private LocalDateTime estimatedEnd;
}
