package com.maison.vinitrackpro.model;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String title;
    
    @Enumerated(EnumType.STRING)
    private ReportType type;
    
    private LocalDate reportDate;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    
    @Column(length = 5000)
    private String summary;
    
    @Column(length = 5000)
    private String findings;
    
    @Column(length = 5000)
    private String recommendations;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by")
    private User generatedBy;
    
    private LocalDateTime createdAt;
    
    // Getters, setters, constructors
}
