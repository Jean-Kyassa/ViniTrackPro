package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.CategoryDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        auditLogService.logActivity("CREATE", "Category", createdCategory.getId(),
                "Created new category: " + createdCategory.getName());
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        auditLogService.logActivity("UPDATE", "Category", id,
                "Updated category: " + updatedCategory.getName());
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        auditLogService.logActivity("DELETE", "Category", id, "Deleted category");
        return ResponseEntity.noContent().build();
    }
}
