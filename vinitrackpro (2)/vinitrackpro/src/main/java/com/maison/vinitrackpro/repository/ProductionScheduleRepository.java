package com.maison.vinitrackpro.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.ProductionSchedule;
import com.maison.vinitrackpro.model.ScheduleStatus;


@Repository
public interface ProductionScheduleRepository extends JpaRepository<ProductionSchedule, Long> {
    List<ProductionSchedule> findByStatus(ScheduleStatus status);
    
    @Query("SELECT ps FROM ProductionSchedule ps WHERE ps.line.id = :lineId AND " +
           "((ps.startTime BETWEEN :start AND :end) OR (ps.endTime BETWEEN :start AND :end))")
    List<ProductionSchedule> findByLineAndTimeRange(
            @Param("lineId") Long lineId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    
    List<ProductionSchedule> findByCreatedBy_Id(Long userId);
    
    @Query("SELECT ps FROM ProductionSchedule ps WHERE ps.status IN :statuses")
    List<ProductionSchedule> findByStatuses(@Param("statuses") List<ScheduleStatus> statuses);
}