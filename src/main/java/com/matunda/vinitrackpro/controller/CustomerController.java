package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.CustomerDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CustomerDTO>> getActiveCustomers() {
        return ResponseEntity.ok(customerService.getActiveCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String query) {
        return ResponseEntity.ok(customerService.searchCustomers(query));
    }

    @PostMapping
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('SALES_STAFF')")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        auditLogService.logActivity("CREATE", "Customer", createdCustomer.getId(),
                "Created new customer: " + createdCustomer.getName());
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('SALES_STAFF')")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        auditLogService.logActivity("UPDATE", "Customer", id,
                "Updated customer: " + updatedCustomer.getName());
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        auditLogService.logActivity("DELETE", "Customer", id, "Deleted customer");
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('SALES_STAFF')")
    public ResponseEntity<CustomerDTO> toggleCustomerStatus(@PathVariable Long id) {
        CustomerDTO updatedCustomer = customerService.toggleCustomerStatus(id);
        String status = updatedCustomer.isActive() ? "activated" : "deactivated";
        auditLogService.logActivity("UPDATE", "Customer", id,
                "Customer " + updatedCustomer.getName() + " " + status);
        return ResponseEntity.ok(updatedCustomer);
    }
}
