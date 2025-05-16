package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.SupplierDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;
    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/active")
    public ResponseEntity<List<SupplierDTO>> getActiveSuppliers() {
        return ResponseEntity.ok(supplierService.getActiveSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SupplierDTO>> searchSuppliers(@RequestParam String query) {
        return ResponseEntity.ok(supplierService.searchSuppliers(query));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<SupplierDTO> createSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        SupplierDTO createdSupplier = supplierService.createSupplier(supplierDTO);
        auditLogService.logActivity("CREATE", "Supplier", createdSupplier.getId(),
                "Created new supplier: " + createdSupplier.getName());
        return new ResponseEntity<>(createdSupplier, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierDTO supplierDTO) {
        SupplierDTO updatedSupplier = supplierService.updateSupplier(id, supplierDTO);
        auditLogService.logActivity("UPDATE", "Supplier", id,
                "Updated supplier: " + updatedSupplier.getName());
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        auditLogService.logActivity("DELETE", "Supplier", id, "Deleted supplier");
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<SupplierDTO> toggleSupplierStatus(@PathVariable Long id) {
        SupplierDTO updatedSupplier = supplierService.toggleSupplierStatus(id);
        String status = updatedSupplier.isActive() ? "activated" : "deactivated";
        auditLogService.logActivity("UPDATE", "Supplier", id,
                "Supplier " + updatedSupplier.getName() + " " + status);
        return ResponseEntity.ok(updatedSupplier);
    }
}
