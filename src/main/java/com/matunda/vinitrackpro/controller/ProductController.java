package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.ProductDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductDTO>> getActiveProducts() {
        return ResponseEntity.ok(productService.getActiveProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query) {
        return ResponseEntity.ok(productService.searchProducts(query));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        auditLogService.logActivity("CREATE", "Product", createdProduct.getId(),
                "Created new product: " + createdProduct.getName());
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        auditLogService.logActivity("UPDATE", "Product", id,
                "Updated product: " + updatedProduct.getName());
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        auditLogService.logActivity("DELETE", "Product", id, "Deleted product");
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<ProductDTO> toggleProductStatus(@PathVariable Long id) {
        ProductDTO updatedProduct = productService.toggleProductStatus(id);
        String status = updatedProduct.isActive() ? "activated" : "deactivated";
        auditLogService.logActivity("UPDATE", "Product", id,
                "Product " + updatedProduct.getName() + " " + status);
        return ResponseEntity.ok(updatedProduct);
    }
}
