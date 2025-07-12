package com.maison.vinitrackpro.model;
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
@Table(name = "quality_checks")
public class QualityCheck {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    @Enumerated(EnumType.STRING)
    private QualityCheckType type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_batch_id")
    private ProductionBatch batch;
    
    private LocalDateTime checkDate;
    
    @Enumerated(EnumType.STRING)
    private QualityStatus status;
    
    @Column(length = 2000)
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_by")
    private User checkedBy;
    
    private LocalDateTime createdAt;
    
    // Getters, setters, constructors
}
