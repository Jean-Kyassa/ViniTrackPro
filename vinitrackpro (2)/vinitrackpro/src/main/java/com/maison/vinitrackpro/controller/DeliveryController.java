package com.maison.vinitrackpro.controller;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maison.vinitrackpro.dto.DeliveryDTO;
import com.maison.vinitrackpro.model.OrderStatus;
import com.maison.vinitrackpro.service.DeliveryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/deliveries")
// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@CrossOrigin(
    origins = {"http://localhost:5173", "http://localhost:3000"},
    allowCredentials = "true"
)
public class DeliveryController {

     @Autowired
    private DeliveryService deliveryService;
    
    // Create new delivery order
    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('USER') or hasRole('LOGISTICS')") 
    // public ResponseEntity<DeliveryDTO.DeliveryOrderDTO> createDeliveryOrder(
    //         @Valid @RequestBody DeliveryDTO.DeliveryOrderDTO deliveryOrderDTO) {
    //     try {
    //         DeliveryDTO.DeliveryOrderDTO createdOrder = deliveryService.createDeliveryOrder(deliveryOrderDTO);
    //         return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('USER') or hasRole('LOGISTICS')")
    // public ResponseEntity<?> createDeliveryOrder(
    //         @Valid @RequestBody DeliveryDTO.DeliveryOrderDTO deliveryOrderDTO) {
    //     try {
    //         // Validate required fields
    //         if (deliveryOrderDTO.getCustomerId() == null) {
    //             return ResponseEntity.badRequest().body("Customer ID is required");
    //         }
    //         if (deliveryOrderDTO.getDeliveryAddress() == null || deliveryOrderDTO.getDeliveryAddress().trim().isEmpty()) {
    //             return ResponseEntity.badRequest().body("Delivery address is required");
    //         }
            
    //         DeliveryDTO.DeliveryOrderDTO createdOrder = deliveryService.createDeliveryOrder(deliveryOrderDTO);
    //         return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //             .body("Error creating delivery order: " + e.getMessage());
    //     }
    // }

    @PostMapping
@PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('USER') or hasRole('LOGISTICS')")
public ResponseEntity<?> createDeliveryOrder(
        @Valid @RequestBody DeliveryDTO.DeliveryOrderDTO deliveryOrderDTO) {
    try {
        // Log the incoming request for debugging
        System.out.println("Received delivery order request: " + deliveryOrderDTO.toString());
        
        // Validate required fields
        if (deliveryOrderDTO.getCustomerId() == null || deliveryOrderDTO.getCustomerId() == 0) {
            return ResponseEntity.badRequest().body("Customer ID is required");
        }
        if (deliveryOrderDTO.getDeliveryAddress() == null || deliveryOrderDTO.getDeliveryAddress().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Delivery address is required");
        }
        if (deliveryOrderDTO.getTotalAmount() <= 0) {
            return ResponseEntity.badRequest().body("Total amount must be greater than 0");
        }
        
        // Generate order number if not provided
        if (deliveryOrderDTO.getOrderNumber() == null || deliveryOrderDTO.getOrderNumber().trim().isEmpty()) {
            deliveryOrderDTO.setOrderNumber("ORD-" + System.currentTimeMillis());
        }
        
        DeliveryDTO.DeliveryOrderDTO createdOrder = deliveryService.createDeliveryOrder(deliveryOrderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    } catch (RuntimeException e) {
        System.err.println("Runtime error creating delivery order: " + e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        System.err.println("Error creating delivery order: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error creating delivery order: " + e.getMessage());
    }
}
    
    // Get all delivery orders with pagination
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')") 
    public ResponseEntity<Page<DeliveryDTO.DeliveryOrderDTO>> getAllDeliveryOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                       Sort.by(sortBy).descending() : 
                       Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<DeliveryDTO.DeliveryOrderDTO> orders = deliveryService.getDeliveryOrders(pageable);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get delivery order by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')")
    public ResponseEntity<DeliveryDTO.DeliveryOrderDTO> getDeliveryOrderById(@PathVariable Long id) {
        Optional<DeliveryDTO.DeliveryOrderDTO> order = deliveryService.getDeliveryOrderById(id);
        return order.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // Get delivery order by order number
    @GetMapping("/order-number/{orderNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')")
    public ResponseEntity<DeliveryDTO.DeliveryOrderDTO> getDeliveryOrderByOrderNumber(
            @PathVariable String orderNumber) {
        Optional<DeliveryDTO.DeliveryOrderDTO> order = deliveryService.getDeliveryOrderByOrderNumber(orderNumber);
        return order.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // Update delivery order
    @PutMapping("/{id}")
@PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')")
    public ResponseEntity<DeliveryDTO.DeliveryOrderDTO> updateDeliveryOrder(
            @PathVariable Long id, 
            @Valid @RequestBody DeliveryDTO.DeliveryOrderDTO deliveryOrderDTO) {
        try {
            DeliveryDTO.DeliveryOrderDTO updatedOrder = deliveryService.updateDeliveryOrder(id, deliveryOrderDTO);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update order status
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')")
    public ResponseEntity<DeliveryDTO.DeliveryOrderDTO> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam OrderStatus status) {
        try {
            DeliveryDTO.DeliveryOrderDTO updatedOrder = deliveryService.updateOrderStatus(id, status);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete delivery order
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')")
    public ResponseEntity<HttpStatus> deleteDeliveryOrder(@PathVariable Long id) {
        try {
            deliveryService.deleteDeliveryOrder(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get orders by customer
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')")
    public ResponseEntity<List<DeliveryDTO.DeliveryOrderDTO>> getOrdersByCustomer(
            @PathVariable Long customerId) {
        try {
            List<DeliveryDTO.DeliveryOrderDTO> orders = deliveryService.getOrdersByCustomer(customerId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get orders by status
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')")
    public ResponseEntity<List<DeliveryDTO.DeliveryOrderDTO>> getOrdersByStatus(
            @PathVariable OrderStatus status) {
        try {
            List<DeliveryDTO.DeliveryOrderDTO> orders = deliveryService.getOrdersByStatus(status);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get orders by driver
    @GetMapping("/driver/{driverId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')")
    public ResponseEntity<List<DeliveryDTO.DeliveryOrderDTO>> getOrdersByDriver(
            @PathVariable Long driverId) {
        try {
            List<DeliveryDTO.DeliveryOrderDTO> orders = deliveryService.getOrdersByDriver(driverId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get orders by route
    @GetMapping("/route/{routeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')")
    public ResponseEntity<List<DeliveryDTO.DeliveryOrderDTO>> getOrdersByRoute(
            @PathVariable Long routeId) {
        try {
            List<DeliveryDTO.DeliveryOrderDTO> orders = deliveryService.getOrdersByRoute(routeId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get pending orders
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')")
    public ResponseEntity<List<DeliveryDTO.DeliveryOrderDTO>> getPendingOrders() {
        try {
            List<DeliveryDTO.DeliveryOrderDTO> orders = deliveryService.getPendingOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Search orders
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS') or hasRole('USER')")
    public ResponseEntity<Page<DeliveryDTO.DeliveryOrderDTO>> searchOrders(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                       Sort.by(sortBy).descending() : 
                       Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<DeliveryDTO.DeliveryOrderDTO> orders = deliveryService.searchOrders(keyword, pageable);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get orders by date range
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS') or hasRole('USER')")
    public ResponseEntity<List<DeliveryDTO.DeliveryOrderDTO>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<DeliveryDTO.DeliveryOrderDTO> orders = deliveryService.getOrdersByDateRange(startDate, endDate);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get delivery statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<DeliveryService.DeliveryStatistics> getDeliveryStatistics() {
        try {
            DeliveryService.DeliveryStatistics statistics = deliveryService.getDeliveryStatistics();
            return new ResponseEntity<>(statistics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Bulk update order status
    @PatchMapping("/bulk-status-update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')")
    public ResponseEntity<String> bulkUpdateOrderStatus(
            @RequestParam List<Long> orderIds,
            @RequestParam OrderStatus status) {
        try {
            int updatedCount = 0;
            for (Long orderId : orderIds) {
                try {
                    deliveryService.updateOrderStatus(orderId, status);
                    updatedCount++;
                } catch (RuntimeException e) {
                    // Continue with other orders if one fails
                }
            }
            return new ResponseEntity<>("Updated " + updatedCount + " orders", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Bulk update failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Mark order as delivered
    @PatchMapping("/{id}/deliver")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS')")
    public ResponseEntity<DeliveryDTO.DeliveryOrderDTO> markAsDelivered(@PathVariable Long id) {
        try {
            DeliveryDTO.DeliveryOrderDTO updatedOrder = deliveryService.updateOrderStatus(id, OrderStatus.DELIVERED);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Cancel order
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY') or hasRole('LOGISTICS') or hasRole('USER')")
    public ResponseEntity<DeliveryDTO.DeliveryOrderDTO> cancelOrder(@PathVariable Long id) {
        try {
            DeliveryDTO.DeliveryOrderDTO updatedOrder = deliveryService.updateOrderStatus(id, OrderStatus.CANCELLED);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Start processing order
    @PatchMapping("/{id}/process")
    public ResponseEntity<DeliveryDTO.DeliveryOrderDTO> startProcessing(@PathVariable Long id) {
        try {
            DeliveryDTO.DeliveryOrderDTO updatedOrder = deliveryService.updateOrderStatus(id, OrderStatus.PROCESSING);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Mark order as in transit
    @PatchMapping("/{id}/transit")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS')")
    public ResponseEntity<DeliveryDTO.DeliveryOrderDTO> markInTransit(@PathVariable Long id) {
        try {
            DeliveryDTO.DeliveryOrderDTO updatedOrder = deliveryService.updateOrderStatus(id, OrderStatus.IN_TRANSIT);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get orders count by status
    @GetMapping("/count")
@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderCountResponse> getOrdersCount() {
        try {
            DeliveryService.DeliveryStatistics stats = deliveryService.getDeliveryStatistics();
            OrderCountResponse response = new OrderCountResponse(
                stats.getTotalOrders(),
                stats.getPendingOrders(),
                stats.getProcessingOrders(),
                stats.getInTransitOrders(),
                stats.getDeliveredOrders(),
                stats.getCancelledOrders()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Inner class for order count response
    public static class OrderCountResponse {
        private long total;
        private long pending;
        private long processing;
        private long inTransit;
        private long delivered;
        private long cancelled;
        
        public OrderCountResponse(long total, long pending, long processing, 
                                long inTransit, long delivered, long cancelled) {
            this.total = total;
            this.pending = pending;
            this.processing = processing;
            this.inTransit = inTransit;
            this.delivered = delivered;
            this.cancelled = cancelled;
        }
        
        // Getters
        public long getTotal() { return total; }
        public long getPending() { return pending; }
        public long getProcessing() { return processing; }
        public long getInTransit() { return inTransit; }
        public long getDelivered() { return delivered; }
        public long getCancelled() { return cancelled; }
    }
    
}
