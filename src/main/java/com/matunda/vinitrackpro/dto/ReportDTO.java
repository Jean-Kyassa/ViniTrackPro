package com.matunda.vinitrackpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private Long id;

    @NotBlank(message = "Report title is required")
    private String title;
    
    private String description;
    
    private String reportType;
    
    private String reportData;
    
    private String fileFormat;
    
    private String fileUrl;
    
    private Long generatedBy;
    
    private String generatedByName;
    
    private LocalDateTime generatedAt;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
