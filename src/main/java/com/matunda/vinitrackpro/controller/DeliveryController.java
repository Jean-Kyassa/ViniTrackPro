package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.DeliveryDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAllDeliveries() {
        return ResponseEntity.ok(deliveryService.getAllDeliveries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getDeliveryById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getDeliveryById(id));
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<DeliveryDTO> getDeliveryByTrackingNumber(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(deliveryService.getDeliveryByTrackingNumber(trackingNumber));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<DeliveryDTO> getDeliveryByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(deliveryService.getDeliveryByOrder(orderId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeliveryDTO>> getDeliveriesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(deliveryService.getDeliveriesByStatus(status));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_STAFF')")
    public ResponseEntity<DeliveryDTO> createDelivery(@Valid @RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO createdDelivery = deliveryService.createDelivery(deliveryDTO);
        auditLogService.logActivity("CREATE", "Delivery", createdDelivery.getId(),
                "Created new delivery with tracking number: " + createdDelivery.getTrackingNumber());
        return new ResponseEntity<>(createdDelivery, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_STAFF')")
    public ResponseEntity<DeliveryDTO> updateDelivery(@PathVariable Long id, @Valid @RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO updatedDelivery = deliveryService.updateDelivery(id, deliveryDTO);
        auditLogService.logActivity("UPDATE", "Delivery", id,
                "Updated delivery with tracking number: " + updatedDelivery.getTrackingNumber());
        return ResponseEntity.ok(updatedDelivery);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_STAFF')")
    public ResponseEntity<DeliveryDTO> updateDeliveryStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        DeliveryDTO updatedDelivery = deliveryService.updateDeliveryStatus(id, status, notes);
        auditLogService.logActivity("UPDATE", "Delivery", id,
                "Updated delivery status to " + status + " for tracking number: " + updatedDelivery.getTrackingNumber());
        return ResponseEntity.ok(updatedDelivery);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        auditLogService.logActivity("DELETE", "Delivery", id, "Deleted delivery");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/assign-driver")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_STAFF')")
    public ResponseEntity<DeliveryDTO> assignDriver(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        DeliveryDTO updatedDelivery = deliveryService.assignDriver(id, driverId);
        auditLogService.logActivity("UPDATE", "Delivery", id,
                "Assigned driver to delivery with tracking number: " + updatedDelivery.getTrackingNumber());
        return ResponseEntity.ok(updatedDelivery);
    }

    @PostMapping("/{id}/assign-vehicle")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_STAFF')")
    public ResponseEntity<DeliveryDTO> assignVehicle(
            @PathVariable Long id,
            @RequestParam Long vehicleId) {
        DeliveryDTO updatedDelivery = deliveryService.assignVehicle(id, vehicleId);
        auditLogService.logActivity("UPDATE", "Delivery", id,
                "Assigned vehicle to delivery with tracking number: " + updatedDelivery.getTrackingNumber());
        return ResponseEntity.ok(updatedDelivery);
    }
}
