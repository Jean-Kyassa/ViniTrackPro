package com.maison.vinitrackpro.dto;
import java.time.LocalDateTime;

import com.maison.vinitrackpro.model.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionTaskDTO {

    private Long id;
    private String name;
    private String description;
    private Long productId;
    private String productName;
    private int quantity;
    private int priority;
    private TaskStatus status;
    private LocalDateTime estimatedStart;
    private LocalDateTime estimatedEnd;
    
    // Getters and setters
    // Constructors
}
