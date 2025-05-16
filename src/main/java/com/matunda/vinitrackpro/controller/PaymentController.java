package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.PaymentDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FINANCE_STAFF')")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FINANCE_STAFF') or hasRole('SALES_STAFF')")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FINANCE_STAFF') or hasRole('SALES_STAFF')")
    public ResponseEntity<PaymentDTO> getPaymentByTransactionId(@PathVariable String transactionId) {
        return ResponseEntity.ok(paymentService.getPaymentByTransactionId(transactionId));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FINANCE_STAFF') or hasRole('SALES_STAFF')")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentsByOrder(orderId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FINANCE_STAFF')")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(paymentService.getPaymentsByStatus(status));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FINANCE_STAFF') or hasRole('SALES_STAFF')")
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
        auditLogService.logActivity("CREATE", "Payment", createdPayment.getId(),
                "Created new payment with transaction ID: " + createdPayment.getTransactionId());
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FINANCE_STAFF')")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable Long id, @Valid @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO updatedPayment = paymentService.updatePayment(id, paymentDTO);
        auditLogService.logActivity("UPDATE", "Payment", id,
                "Updated payment with transaction ID: " + updatedPayment.getTransactionId());
        return ResponseEntity.ok(updatedPayment);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FINANCE_STAFF')")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        PaymentDTO updatedPayment = paymentService.updatePaymentStatus(id, status, notes);
        auditLogService.logActivity("UPDATE", "Payment", id,
                "Updated payment status to " + status + " for transaction ID: " + updatedPayment.getTransactionId());
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        auditLogService.logActivity("DELETE", "Payment", id, "Deleted payment");
        return ResponseEntity.noContent().build();
    }
}
