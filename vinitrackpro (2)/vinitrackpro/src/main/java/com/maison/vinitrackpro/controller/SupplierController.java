package com.maison.vinitrackpro.controller;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maison.vinitrackpro.dto.SupplierDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.Currency;
import com.maison.vinitrackpro.model.PaymentTerms;
import com.maison.vinitrackpro.model.ProductCategory;
import com.maison.vinitrackpro.service.SupplierService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/suppliers")
// @CrossOrigin(origins = "*", maxAge = 3600)
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
     @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('USER')")
    public ResponseEntity<Page<SupplierDTO>> getAllSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ProductCategory category) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SupplierDTO> suppliers = supplierService.getAllSuppliers(pageable, search, category);
        
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('USER')")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        try {
            SupplierDTO supplier = supplierService.getSupplierById(id);
            return ResponseEntity.ok(supplier);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSupplier(@Valid @RequestBody SupplierDTO supplierDTO, 
                                          Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        try {
            String username = authentication.getName();
            SupplierDTO createdSupplier = supplierService.createSupplier(supplierDTO, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSupplier);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error occurred while creating supplier: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSupplier(@PathVariable Long id, 
                                          @Valid @RequestBody SupplierDTO supplierDTO) {
        try {
            SupplierDTO updatedSupplier = supplierService.updateSupplier(id, supplierDTO);
            return ResponseEntity.ok(updatedSupplier);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error occurred while updating supplier: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Supplier deleted successfully!");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error occurred while deleting supplier: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<Page<SupplierDTO>> searchSuppliers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "companyName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SupplierDTO> suppliers = supplierService.getAllSuppliers(pageable, query, null);
        
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<Page<SupplierDTO>> getSuppliersByCategory(
            @PathVariable ProductCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "companyName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SupplierDTO> suppliers = supplierService.getAllSuppliers(pageable, null, category);
        
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/product-categories")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<ProductCategory[]> getAllProductCategories() {
        return ResponseEntity.ok(ProductCategory.values());
    }

    @GetMapping("/currencies")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('USER')")
    public ResponseEntity<Currency[]> getAllCurrencies() {
        return ResponseEntity.ok(Currency.values());
    }

    @GetMapping("/payment-terms")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<PaymentTerms[]> getAllPaymentTerms() {
        return ResponseEntity.ok(PaymentTerms.values());
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSupplierStats() {
        try {
            Page<SupplierDTO> allSuppliers = supplierService.getAllSuppliers(
                PageRequest.of(0, Integer.MAX_VALUE), null, null);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalSuppliers", allSuppliers.getTotalElements());
            
            // Calculate average lead time
            double avgLeadTime = allSuppliers.getContent().stream()
                .mapToInt(SupplierDTO::getLeadTime)
                .average()
                .orElse(0.0);
            stats.put("averageLeadTime", avgLeadTime);
            
            // Count suppliers by category
            Map<ProductCategory, Long> categoryCount = new HashMap<>();
            for (ProductCategory category : ProductCategory.values()) {
                long count = allSuppliers.getContent().stream()
                    .filter(supplier -> supplier.getProductCategories() == category)
                    .count();
                categoryCount.put(category, count);
            }
            stats.put("suppliersByCategory", categoryCount);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error occurred while fetching supplier stats: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/lead-time")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public ResponseEntity<Page<SupplierDTO>> getSuppliersByLeadTime(
            @RequestParam(defaultValue = "0") int minLeadTime,
            @RequestParam(defaultValue = "365") int maxLeadTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "leadTime") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SupplierDTO> allSuppliers = supplierService.getAllSuppliers(pageable, null, null);
        
        // Filter by lead time (this could be moved to service layer for better performance)
        Page<SupplierDTO> filteredSuppliers = allSuppliers.map(supplier -> {
            if (supplier.getLeadTime() >= minLeadTime && supplier.getLeadTime() <= maxLeadTime) {
                return supplier;
            }
            return null;
        });
        
        return ResponseEntity.ok(filteredSuppliers);
    }
}
