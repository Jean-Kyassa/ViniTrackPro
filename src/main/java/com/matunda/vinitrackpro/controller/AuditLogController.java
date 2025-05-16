package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.AuditLogDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<Page<AuditLogDTO>> getAuditLogs(Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getAuditLogs(pageable));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByUser(userId));
    }

    @GetMapping("/entity-type/{entityType}")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByEntityType(@PathVariable String entityType) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByEntityType(entityType));
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByEntityId(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByEntityId(entityType, entityId));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByDateRange(startDate, endDate));
    }
}
