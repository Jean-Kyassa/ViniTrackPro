package com.maison.vinitrackpro.dto;

import java.time.LocalDateTime;

import com.maison.vinitrackpro.model.QualityCheckType;
import com.maison.vinitrackpro.model.QualityStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateQualityCheckDTO {

    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Type is required")
    private QualityCheckType type;
    
    @NotNull(message = "Batch ID is required")
    private Long batchId;
    
    private LocalDateTime checkDate;
    
    @NotNull(message = "Status is required")
    private QualityStatus status;
    
    @Size(max = 2000, message = "Notes cannot exceed 2000 characters")
    private String notes;
    
    @NotNull(message = "Checked by user ID is required")
    private Long checkedById;
    
    // Constructors
    public CreateQualityCheckDTO() {}
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public QualityCheckType getType() { return type; }
    public void setType(QualityCheckType type) { this.type = type; }
    
    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }
    
    public LocalDateTime getCheckDate() { return checkDate; }
    public void setCheckDate(LocalDateTime checkDate) { this.checkDate = checkDate; }
    
    public QualityStatus getStatus() { return status; }
    public void setStatus(QualityStatus status) { this.status = status; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Long getCheckedById() { return checkedById; }
    public void setCheckedById(Long checkedById) { this.checkedById = checkedById; }
}
