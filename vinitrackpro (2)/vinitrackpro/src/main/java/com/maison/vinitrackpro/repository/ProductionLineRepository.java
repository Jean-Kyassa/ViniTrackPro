package com.maison.vinitrackpro.repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.LineStatus;
import com.maison.vinitrackpro.model.LineType;
import com.maison.vinitrackpro.model.ProductionLine;

@Repository
public interface ProductionLineRepository extends JpaRepository<ProductionLine, Long> {

    // Find production line by exact name
    Optional<ProductionLine> findByName(String name);
    
    // Find production lines by type
    List<ProductionLine> findByType(LineType type);
    
    // Find production lines by status
    List<ProductionLine> findByStatus(LineStatus status);
    
    // Find production lines that need maintenance (efficiency below threshold)
    @Query("SELECT pl FROM ProductionLine pl WHERE pl.efficiency < :threshold")
    List<ProductionLine> findLinesNeedingMaintenance(@Param("threshold") double efficiencyThreshold);
    
    // Find production lines that haven't had maintenance in X days
    @Query("SELECT pl FROM ProductionLine pl WHERE pl.lastMaintenance < :cutoffDate")
    List<ProductionLine> findLinesDueForMaintenance(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // Find operational production lines of a specific type
    List<ProductionLine> findByTypeAndStatus(LineType type, LineStatus status);
    
    // Find production lines by name containing (case-insensitive)
    List<ProductionLine> findByNameContainingIgnoreCase(String name);
    
    // Check if production line with given name exists (for validation)
    boolean existsByName(String name);
}