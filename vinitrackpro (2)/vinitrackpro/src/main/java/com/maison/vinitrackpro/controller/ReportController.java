package com.maison.vinitrackpro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maison.vinitrackpro.dto.ReportDTO;
import com.maison.vinitrackpro.model.ReportType;
import com.maison.vinitrackpro.service.ReportService;

@RestController
@RequestMapping("/api/reports")
// @CrossOrigin(origins = "*")
public class ReportController {

     private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('QUALITY') or hasRole('LOGISTICS')") 
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('QUALITY') or hasRole('LOGISTICS')") 
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('QUALITY') or hasRole('LOGISTICS')") 
    public ResponseEntity<ReportDTO> createReport(
            @RequestBody ReportDTO reportDTO,
            @RequestHeader("X-User-Id") Long generatedById) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportService.createReport(reportDTO, generatedById));
    }

    @PutMapping("/{id}")
@PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('QUALITY') or hasRole('LOGISTICS')")
    public ResponseEntity<ReportDTO> updateReport(
            @PathVariable Long id,
            @RequestBody ReportDTO reportDTO) {
        return ResponseEntity.ok(reportService.updateReport(id, reportDTO));
    }

    @DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('QUALITY') or hasRole('LOGISTICS')")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
@PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('QUALITY') or hasRole('LOGISTICS')")
    public ResponseEntity<List<ReportDTO>> getReportsByType(@PathVariable ReportType type) {
        return ResponseEntity.ok(reportService.getReportsByType(type));
    }

    @GetMapping("/user/{userId}")
@PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('QUALITY') or hasRole('LOGISTICS')")
    public ResponseEntity<List<ReportDTO>> getReportsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getReportsByUser(userId));
    }

    // @GetMapping("/date-range")
    // public ResponseEntity<List<ReportDTO>> getReportsByDateRange(
    //         @RequestParam LocalDate start,
    //         @RequestParam LocalDate end) {
    //     return ResponseEntity.ok(reportService.getReportsByDateRange(start, end));
    // }
}
