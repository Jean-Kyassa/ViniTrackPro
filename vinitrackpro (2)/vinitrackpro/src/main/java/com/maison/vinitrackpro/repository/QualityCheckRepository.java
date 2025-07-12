package com.maison.vinitrackpro.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.QualityCheck;
import com.maison.vinitrackpro.model.QualityCheckType;
import com.maison.vinitrackpro.model.QualityStatus;

@Repository
public interface QualityCheckRepository extends JpaRepository<QualityCheck, Long> {
    
    List<QualityCheck> findByBatchId(Long batchId);
    
    List<QualityCheck> findByStatus(QualityStatus status);
    
    List<QualityCheck> findByType(QualityCheckType type);
    
    List<QualityCheck> findByCheckedById(Long checkedById);
    
    @Query("SELECT qc FROM QualityCheck qc WHERE qc.checkDate BETWEEN :startDate AND :endDate")
    List<QualityCheck> findByCheckDateBetween(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT qc FROM QualityCheck qc JOIN FETCH qc.batch b WHERE b.batchNumber = :batchNumber")
    List<QualityCheck> findByBatchNumber(@Param("batchNumber") String batchNumber);
    
    @Query("SELECT qc FROM QualityCheck qc WHERE qc.batch.id = :batchId AND qc.status = :status")
    List<QualityCheck> findByBatchIdAndStatus(@Param("batchId") Long batchId, 
                                             @Param("status") QualityStatus status);
    
    @Query("SELECT COUNT(qc) FROM QualityCheck qc WHERE qc.status = :status")
    long countByStatus(@Param("status") QualityStatus status);
    
    @Query("SELECT qc FROM QualityCheck qc WHERE qc.name LIKE %:name%")
    List<QualityCheck> findByNameContainingIgnoreCase(@Param("name") String name);
    
    Page<QualityCheck> findByStatusOrderByCheckDateDesc(QualityStatus status, Pageable pageable);
}