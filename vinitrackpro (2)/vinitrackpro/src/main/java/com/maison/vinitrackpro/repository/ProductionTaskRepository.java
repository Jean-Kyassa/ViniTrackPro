package com.maison.vinitrackpro.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.ProductionTask;
import com.maison.vinitrackpro.model.TaskPriority;
import com.maison.vinitrackpro.model.TaskStatus;

@Repository
public interface ProductionTaskRepository  extends JpaRepository<ProductionTask, Long> {
    
    List<ProductionTask> findByScheduleId(Long scheduleId);
        
    List<ProductionTask> findByStatus(TaskStatus status);
        
    List<ProductionTask> findByPriority(TaskPriority priority);
        
    //List<ProductionTask> findByAssignedToId(Long assignedToId);
        
    // Change this line - use the correct property path
    List<ProductionTask> findByProductProductId(Long productId);
    // @Query("SELECT pt FROM ProductionTask pt WHERE pt.plannedStartTime BETWEEN :startDate AND :endDate")
    // List<ProductionTask> findByPlannedStartTimeBetween(@Param("startDate") LocalDateTime startDate, 
    //                                                   @Param("endDate") LocalDateTime endDate);
    
    // @Query("SELECT pt FROM ProductionTask pt WHERE pt.status = :status AND pt.assignedTo.id = :userId")
    // List<ProductionTask> findByStatusAndAssignedToId(@Param("status") TaskStatus status, 
    //                                                  @Param("userId") Long userId);
    
    // @Query("SELECT pt FROM ProductionTask pt WHERE pt.plannedStartTime <= :currentTime AND pt.status = 'PENDING'")
    // List<ProductionTask> findOverdueTasks(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT COUNT(pt) FROM ProductionTask pt WHERE pt.status = :status")
    long countByStatus(@Param("status") TaskStatus status);
}