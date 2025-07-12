package com.maison.vinitrackpro.model;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "production_lines")
public class ProductionLine {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    @Enumerated(EnumType.STRING)
    private LineType type;
    
    private double efficiency; // percentage
    private LocalDateTime lastMaintenance;
    
    @Enumerated(EnumType.STRING)
    private LineStatus status;
    
    // Getters, setters, constructors
}
