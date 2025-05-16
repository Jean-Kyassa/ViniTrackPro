package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.InventoryDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InventoryDTO>> getInventoryByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProduct(productId));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryDTO>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<InventoryDTO>> getExpiringItems(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(inventoryService.getExpiringItems(days));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<InventoryDTO> createInventory(@Valid @RequestBody InventoryDTO inventoryDTO) {
        InventoryDTO createdInventory = inventoryService.createInventory(inventoryDTO);
        auditLogService.logActivity("CREATE", "Inventory", createdInventory.getId(),
                "Created inventory for product: " + createdInventory.getProductName());
        return new ResponseEntity<>(createdInventory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<InventoryDTO> updateInventory(@PathVariable Long id, @Valid @RequestBody InventoryDTO inventoryDTO) {
        InventoryDTO updatedInventory = inventoryService.updateInventory(id, inventoryDTO);
        auditLogService.logActivity("UPDATE", "Inventory", id,
                "Updated inventory for product: " + updatedInventory.getProductName());
        return ResponseEntity.ok(updatedInventory);
    }

    @PatchMapping("/{id}/adjust-quantity")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('WAREHOUSE_STAFF')")
    public ResponseEntity<InventoryDTO> adjustInventoryQuantity(
            @PathVariable Long id,
            @RequestParam Long quantity,
            @RequestParam String reason) {
        InventoryDTO updatedInventory = inventoryService.adjustInventoryQuantity(id, quantity, Long.valueOf(reason));
        auditLogService.logActivity("UPDATE", "Inventory", id,
                "Adjusted inventory quantity for product: " + updatedInventory.getProductName() +
                        " by " + quantity + ". Reason: " + reason);
        return ResponseEntity.ok(updatedInventory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        auditLogService.logActivity("DELETE", "Inventory", id, "Deleted inventory record");
        return ResponseEntity.noContent().build();
    }
}
