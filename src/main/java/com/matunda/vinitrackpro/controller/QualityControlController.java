package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.QualityControlDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.QualityControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/quality-controls")
@RequiredArgsConstructor
public class QualityControlController {

    private final QualityControlService qualityControlService;
    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<QualityControlDTO>> getAllQualityControls() {
        return ResponseEntity.ok(qualityControlService.getAllQualityControls());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QualityControlDTO> getQualityControlById(@PathVariable Long id) {
        return ResponseEntity.ok(qualityControlService.getQualityControlById(id));
    }

    @GetMapping("/batch/{batchNumber}")
    public ResponseEntity<QualityControlDTO> getQualityControlByBatchNumber(@PathVariable String batchNumber) {
        return ResponseEntity.ok(qualityControlService.getQualityControlByBatchNumber(batchNumber));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<QualityControlDTO>> getQualityControlsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(qualityControlService.getQualityControlsByProduct(productId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<QualityControlDTO>> getQualityControlsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(qualityControlService.getQualityControlsByStatus(status));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_CONTROL_STAFF')")
    public ResponseEntity<QualityControlDTO> createQualityControl(@Valid @RequestBody QualityControlDTO qualityControlDTO) {
        QualityControlDTO createdQC = qualityControlService.createQualityControl(qualityControlDTO);
        auditLogService.logActivity("CREATE", "QualityControl", createdQC.getId(),
                "Created new quality control check for batch: " + createdQC.getBatchNumber());
        return new ResponseEntity<>(createdQC, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_CONTROL_STAFF')")
    public ResponseEntity<QualityControlDTO> updateQualityControl(@PathVariable Long id, @Valid @RequestBody QualityControlDTO qualityControlDTO) {
        QualityControlDTO updatedQC = qualityControlService.updateQualityControl(id, qualityControlDTO);
        auditLogService.logActivity("UPDATE", "QualityControl", id,
                "Updated quality control check for batch: " + updatedQC.getBatchNumber());
        return ResponseEntity.ok(updatedQC);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_CONTROL_STAFF')")
    public ResponseEntity<QualityControlDTO> updateQualityControlStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        QualityControlDTO updatedQC = qualityControlService.updateQualityControlStatus(id, status, notes);
        auditLogService.logActivity("UPDATE", "QualityControl", id,
                "Updated quality control status to " + status + " for batch: " + updatedQC.getBatchNumber());
        return ResponseEntity.ok(updatedQC);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteQualityControl(@PathVariable Long id) {
        qualityControlService.deleteQualityControl(id);
        auditLogService.logActivity("DELETE", "QualityControl", id, "Deleted quality control check");
        return ResponseEntity.noContent().build();
    }
}
