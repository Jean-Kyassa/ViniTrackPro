package com.maison.vinitrackpro.repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.Report;
import com.maison.vinitrackpro.model.ReportType;
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByType(ReportType type);
    
    List<Report> findByGeneratedBy_Id(Long userId);
    
    @Query("SELECT r FROM Report r WHERE r.reportDate BETWEEN :startDate AND :endDate")
    List<Report> findByReportDateBetween(@Param("startDate") LocalDate startDate, 
                                        @Param("endDate") LocalDate endDate);
    
    @Query("SELECT r FROM Report r WHERE r.periodStart <= :date AND r.periodEnd >= :date")
    List<Report> findReportsCoveringDate(@Param("date") LocalDate date);
    
    List<Report> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT r FROM Report r WHERE r.type = :type AND r.createdAt >= :since")
    List<Report> findRecentReportsByType(@Param("type") ReportType type, 
                                       @Param("since") LocalDateTime since);
}