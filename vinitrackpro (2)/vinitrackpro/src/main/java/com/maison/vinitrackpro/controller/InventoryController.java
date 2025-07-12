package com.maison.vinitrackpro.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maison.vinitrackpro.dto.InventoryItemDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.ItemCategory;
import com.maison.vinitrackpro.model.UnitType;
import com.maison.vinitrackpro.service.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(originPatterns = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173"}, 
             allowCredentials = "true", maxAge = 3600)
public class InventoryController {

     @Autowired
    private InventoryService inventoryService;

    @GetMapping
     @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('USER')")
    public ResponseEntity<Page<InventoryItemDTO>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ItemCategory category) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryItemDTO> items = inventoryService.getAllItems(pageable, search, category);
        
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('USER')")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        try {
            InventoryItemDTO item = inventoryService.getItemById(id);
            return ResponseEntity.ok(item);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<?> createItem(@Valid @RequestBody InventoryItemDTO itemDTO, 
                                       Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        try {
            String username = authentication.getName();
            InventoryItemDTO createdItem = inventoryService.createItem(itemDTO, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error occurred while creating inventory item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

// @PostMapping
// @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
// public ResponseEntity<?> createItem(@Valid @RequestBody InventoryItemDTO itemDTO, 
//                                    Authentication authentication) {
//     try {
//         if (authentication == null || !authentication.isAuthenticated()) {
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//         }

//         String username = authentication.getName();
//         InventoryItemDTO createdItem = inventoryService.createItem(itemDTO, username);
//         return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
//     } catch (ResourceNotFoundException e) {
//         return ResponseEntity.badRequest().body(e.getMessage());
//     } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Error creating item: " + e.getMessage());
//     }
// }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<?> updateItem(@PathVariable Long id, 
                                       @Valid @RequestBody InventoryItemDTO itemDTO) {
        try {
            InventoryItemDTO updatedItem = inventoryService.updateItem(id, itemDTO);
            return ResponseEntity.ok(updatedItem);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error occurred while updating inventory item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            inventoryService.deleteItem(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Inventory item deleted successfully!");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error occurred while deleting inventory item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<Page<InventoryItemDTO>> searchItems(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "itemName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryItemDTO> items = inventoryService.getAllItems(pageable, query, null);
        
        return ResponseEntity.ok(items);
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('USER')")
    public ResponseEntity<Page<InventoryItemDTO>> getItemsByCategory(
            @PathVariable ItemCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "itemName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryItemDTO> items = inventoryService.getAllItems(pageable, null, category);
        
        return ResponseEntity.ok(items);
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<?> getLowStockItems() {
        try {
            List<InventoryItemDTO> lowStockItems = inventoryService.getLowStockItems();
            return ResponseEntity.ok(lowStockItems);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error occurred while fetching low stock items: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<ItemCategory[]> getAllCategories() {
        return ResponseEntity.ok(ItemCategory.values());
    }

    @GetMapping("/unit-types")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<UnitType[]> getAllUnitTypes() {
        return ResponseEntity.ok(UnitType.values());
    }

    @PutMapping("/{id}/quantity")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<?> updateQuantity(@PathVariable Long id, 
                                          @RequestParam int quantity) {
        try {
            InventoryItemDTO item = inventoryService.getItemById(id);
            item.setCurrentQuantity(quantity);
            InventoryItemDTO updatedItem = inventoryService.updateItem(id, item);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Quantity updated successfully!");
            response.put("item", updatedItem);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error occurred while updating quantity: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<?> getInventoryStats() {
        try {
            Page<InventoryItemDTO> allItems = inventoryService.getAllItems(
                PageRequest.of(0, Integer.MAX_VALUE), null, null);
            List<InventoryItemDTO> lowStockItems = inventoryService.getLowStockItems();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalItems", allItems.getTotalElements());
            stats.put("lowStockItems", lowStockItems.size());
            
            // Calculate total value
            double totalValue = allItems.getContent().stream()
                .mapToDouble(item -> item.getCurrentQuantity() * item.getCostPrice())
                .sum();
            stats.put("totalInventoryValue", totalValue);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error occurred while fetching inventory stats: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
