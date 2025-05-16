package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.VehicleDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/active")
    public ResponseEntity<List<VehicleDTO>> getActiveVehicles() {
        return ResponseEntity.ok(vehicleService.getActiveVehicles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @GetMapping("/registration/{registrationNumber}")
    public ResponseEntity<VehicleDTO> getVehicleByRegistrationNumber(@PathVariable String registrationNumber) {
        return ResponseEntity.ok(vehicleService.getVehicleByRegistrationNumber(registrationNumber));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByType(@PathVariable String type) {
        return ResponseEntity.ok(vehicleService.getVehiclesByType(type));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_STAFF')")
    public ResponseEntity<VehicleDTO> createVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO createdVehicle = vehicleService.createVehicle(vehicleDTO);
        auditLogService.logActivity("CREATE", "Vehicle", createdVehicle.getId(),
                "Created new vehicle with registration: " + createdVehicle.getRegistrationNumber());
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_STAFF')")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);
        auditLogService.logActivity("UPDATE", "Vehicle", id,
                "Updated vehicle with registration: " + updatedVehicle.getRegistrationNumber());
        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        auditLogService.logActivity("DELETE", "Vehicle", id, "Deleted vehicle");
        return ResponseEntity.noContent().build();
    }
}
