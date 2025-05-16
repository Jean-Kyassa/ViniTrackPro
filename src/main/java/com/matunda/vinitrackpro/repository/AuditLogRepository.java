package com.matunda.vinitrackpro.repository;

import com.matunda.vinitrackpro.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findAll(Pageable pageable);

    List<AuditLog> findByUserId(Long userId);

    List<AuditLog> findByEntityType(String entityType);

    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);

    List<AuditLog> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
}