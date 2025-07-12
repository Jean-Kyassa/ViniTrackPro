package com.maison.vinitrackpro.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.maison.vinitrackpro.model.ReportType;

public class ReportDTO {
    private Long id;
    private String title;
    private ReportType type;
    private LocalDate reportDate;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String summary;
    private String findings;
    private String recommendations;
    private Long generatedById;
    private String generatedByUsername;
    private LocalDateTime createdAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public ReportType getType() { return type; }
    public void setType(ReportType type) { this.type = type; }
    
    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
    
    public LocalDate getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }
    
    public LocalDate getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }
    
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    
    public String getFindings() { return findings; }
    public void setFindings(String findings) { this.findings = findings; }
    
    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }
    
    public Long getGeneratedById() { return generatedById; }
    public void setGeneratedById(Long generatedById) { this.generatedById = generatedById; }
    
    public String getGeneratedByUsername() { return generatedByUsername; }
    public void setGeneratedByUsername(String generatedByUsername) { this.generatedByUsername = generatedByUsername; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
