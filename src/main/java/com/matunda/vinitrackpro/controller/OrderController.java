package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.OrderDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderDTO> getOrderByOrderNumber(@PathVariable String orderNumber) {
        return ResponseEntity.ok(orderService.getOrderByOrderNumber(orderNumber));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_STAFF')")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        auditLogService.logActivity("CREATE", "Order", createdOrder.getId(),
                "Created new order: " + createdOrder.getOrderNumber());
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_STAFF')")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
        auditLogService.logActivity("UPDATE", "Order", id,
                "Updated order: " + updatedOrder.getOrderNumber());
        return ResponseEntity.ok(updatedOrder);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_STAFF') or hasRole('WAREHOUSE_STAFF')")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
        auditLogService.logActivity("UPDATE", "Order", id,
                "Updated order status to " + status + " for order: " + updatedOrder.getOrderNumber());
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        auditLogService.logActivity("DELETE", "Order", id, "Deleted order");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_STAFF')")
    public ResponseEntity<OrderDTO> cancelOrder(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        OrderDTO cancelledOrder = orderService.cancelOrder(id, reason);
        auditLogService.logActivity("UPDATE", "Order", id,
                "Cancelled order: " + cancelledOrder.getOrderNumber() +
                        (reason != null ? ". Reason: " + reason : ""));
        return ResponseEntity.ok(cancelledOrder);
    }
}
