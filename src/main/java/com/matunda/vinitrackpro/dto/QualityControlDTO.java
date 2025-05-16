package com.matunda.vinitrackpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.matunda.vinitrackpro.model.User;

import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityControlDTO {

    private Long id;
    
    @NotBlank(message = "Batch number is required")
    private String batchNumber;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productName;
    
    private LocalDateTime inspectionDate;

    private User approver;
    
    private String inspectedBy;

    private Long inspectorId;

    private String approvedBy;

    private String appearance;

    private String aroma;

    private String taste;

    private String alcoholContent;

    private String acidity;

    private String sugarContent;
    
    private String status;

    private LocalDateTime approvalDate;
    
    private String notes;
    
    private String testResults;
    
    private LocalDateTime expiryDate;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
