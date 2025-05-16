package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.ReportDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<ReportDTO>> getReportsByType(@PathVariable String type) {
        return ResponseEntity.ok(reportService.getReportsByType(type));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<ReportDTO>> getReportsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getReportsByUser(userId));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<ReportDTO>> getReportsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(reportService.getReportsByDateRange(start, end));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ReportDTO> createReport(@Valid @RequestBody ReportDTO reportDTO) {
        ReportDTO createdReport = reportService.createReport(reportDTO);
        auditLogService.logActivity("CREATE", "Report", createdReport.getId(),
                "Created new report: " + createdReport.getTitle());
        return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ReportDTO> updateReport(@PathVariable Long id, @Valid @RequestBody ReportDTO reportDTO) {
        ReportDTO updatedReport = reportService.updateReport(id, reportDTO);
        auditLogService.logActivity("UPDATE", "Report", id,
                "Updated report: " + updatedReport.getTitle());
        return ResponseEntity.ok(updatedReport);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        auditLogService.logActivity("DELETE", "Report", id, "Deleted report");
        return ResponseEntity.noContent().build();
    }
}
