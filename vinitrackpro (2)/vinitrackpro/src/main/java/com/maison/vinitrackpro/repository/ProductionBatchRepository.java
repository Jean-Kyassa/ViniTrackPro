package com.maison.vinitrackpro.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.ProductionBatch;
import com.maison.vinitrackpro.model.QualityStatus;

@Repository
public interface ProductionBatchRepository extends JpaRepository<ProductionBatch, Long> {
    
    Optional<ProductionBatch> findByBatchNumber(String batchNumber);
    
    List<ProductionBatch> findByProductProductId(Long productId);
    
    List<ProductionBatch> findByCreatedById(Long createdById);
    
    @Query("SELECT pb FROM ProductionBatch pb WHERE pb.productionDate BETWEEN :startDate AND :endDate")
    List<ProductionBatch> findByProductionDateBetween(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);
    
    @Query("SELECT pb FROM ProductionBatch pb WHERE pb.expiryDate BETWEEN :startDate AND :endDate")
    List<ProductionBatch> findByExpiryDateBetween(@Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);
    
    @Query("SELECT pb FROM ProductionBatch pb WHERE pb.expiryDate <= :date")
    List<ProductionBatch> findExpiredBatches(@Param("date") LocalDate date);
    
    @Query("SELECT pb FROM ProductionBatch pb WHERE pb.expiryDate BETWEEN :today AND :futureDate")
    List<ProductionBatch> findBatchesExpiringWithinDays(@Param("today") LocalDate today,
                                                         @Param("futureDate") LocalDate futureDate);
    
    @Query("SELECT pb FROM ProductionBatch pb WHERE pb.quantity >= :minQuantity")
    List<ProductionBatch> findByQuantityGreaterThanEqual(@Param("minQuantity") int minQuantity);
    
    @Query("SELECT pb FROM ProductionBatch pb WHERE pb.quantity <= :maxQuantity")
    List<ProductionBatch> findByQuantityLessThanEqual(@Param("maxQuantity") int maxQuantity);
    
    @Query("SELECT pb FROM ProductionBatch pb WHERE pb.quantity BETWEEN :minQuantity AND :maxQuantity")
    List<ProductionBatch> findByQuantityBetween(@Param("minQuantity") int minQuantity,
                                                @Param("maxQuantity") int maxQuantity);
    
    @Query("SELECT pb FROM ProductionBatch pb JOIN FETCH pb.product p WHERE p.name LIKE %:productName%")
    List<ProductionBatch> findByProductNameContainingIgnoreCase(@Param("productName") String productName);
    
    @Query("SELECT pb FROM ProductionBatch pb JOIN FETCH pb.product p WHERE p.category = :category")
    List<ProductionBatch> findByProductCategory(@Param("category") String category);
    
    @Query("SELECT pb FROM ProductionBatch pb WHERE pb.batchNumber LIKE %:batchNumber%")
    List<ProductionBatch> findByBatchNumberContainingIgnoreCase(@Param("batchNumber") String batchNumber);
    
    @Query("SELECT pb FROM ProductionBatch pb WHERE pb.createdAt BETWEEN :startDate AND :endDate")
    List<ProductionBatch> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(pb) FROM ProductionBatch pb WHERE pb.product.productId = :productId")
    long countByProductId(@Param("productId") Long productId);
    
    @Query("SELECT SUM(pb.quantity) FROM ProductionBatch pb WHERE pb.product.productId = :productId")
    Long getTotalQuantityByProductId(@Param("productId") Long productId);
    
    @Query("SELECT pb FROM ProductionBatch pb WHERE SIZE(pb.qualityChecks) = 0")
    List<ProductionBatch> findBatchesWithoutQualityChecks();
    
    @Query("SELECT pb FROM ProductionBatch pb WHERE EXISTS (SELECT qc FROM QualityCheck qc WHERE qc.batch = pb AND qc.status = :status)")
    List<ProductionBatch> findBatchesWithQualityCheckStatus(@Param("status") QualityStatus status);
    
    @Query("SELECT pb FROM ProductionBatch pb WHERE NOT EXISTS (SELECT qc FROM QualityCheck qc WHERE qc.batch = pb AND qc.status = 'FAILED')")
    List<ProductionBatch> findBatchesWithoutFailedQualityChecks();
    
    Page<ProductionBatch> findByProductProductIdOrderByProductionDateDesc(Long productId, Pageable pageable);
    
    Page<ProductionBatch> findByCreatedByIdOrderByCreatedAtDesc(Long createdById, Pageable pageable);
    
    Page<ProductionBatch> findAllByOrderByProductionDateDesc(Pageable pageable);
    
    boolean existsByBatchNumber(String batchNumber);
    
    @Query("SELECT DISTINCT YEAR(pb.productionDate) FROM ProductionBatch pb ORDER BY YEAR(pb.productionDate) DESC")
    List<Integer> findDistinctProductionYears();
    
    @Query("SELECT DISTINCT MONTH(pb.productionDate) FROM ProductionBatch pb WHERE YEAR(pb.productionDate) = :year ORDER BY MONTH(pb.productionDate)")
    List<Integer> findDistinctProductionMonthsByYear(@Param("year") int year);
}
