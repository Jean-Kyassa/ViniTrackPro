package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.OrderDTO;
import com.matunda.vinitrackpro.dto.OrderItemDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.*;
import com.matunda.vinitrackpro.repository.CustomerRepository;
import com.matunda.vinitrackpro.repository.OrderRepository;
import com.matunda.vinitrackpro.repository.ProductRepository;
import com.matunda.vinitrackpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return convertToDTO(order);
    }

    public OrderDTO getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with number: " + orderNumber));
        return convertToDTO(order);
    }

    public List<OrderDTO> getOrdersByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        return orderRepository.findByCustomer(customer).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByStatus(String status) {
        Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status);
        return orderRepository.findByStatus(orderStatus).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDTO.getCustomerId()));

        User createdBy = null;
        if (orderDTO.getUserId() != null) {  // Changed from getCreatedById to getUserId
            createdBy = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + orderDTO.getUserId()));
        }

        User assignedTo = null;
        if (orderDTO.getUserId() != null) {
            assignedTo = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + orderDTO.getUserId()));
        }

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryDate(orderDTO.getDeliveryDate());
        order.setStatus(Order.OrderStatus.valueOf(orderDTO.getStatus()));
        order.setNotes(orderDTO.getNotes());
        order.setCreatedBy(createdBy);
        order.setAssignedTo(assignedTo);

        // Calculate total amount and create order items
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDTO.getProductId()));
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity().intValue());
            orderItem.setPrice(product.getPrice());
            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());
        }
        
        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        
        // Create notification for new order
        notificationService.createOrderNotification(savedOrder, "New order created");
        
        return convertToDTO(savedOrder);
    }    public OrderDTO updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status);
        Order.OrderStatus oldStatus = order.getStatus();
        
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        
        // Create notification for status change
        notificationService.createOrderNotification(updatedOrder, 
                "Order status changed from " + oldStatus + " to " + newStatus);
        
        return convertToDTO(updatedOrder);
    }

    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDTO.getCustomerId()));

        User assignedTo = null;
        if (orderDTO.getAssignedTo() != null) {
            try {
                Long assignedToId = Long.parseLong(orderDTO.getAssignedTo());
                assignedTo = userRepository.findById(assignedToId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + assignedToId));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid user ID format in assignedTo field");
            }
        }

        order.setCustomer(customer);
        order.setDeliveryDate(orderDTO.getDeliveryDate());
        order.setStatus(Order.OrderStatus.valueOf(orderDTO.getStatus()));
        order.setNotes(orderDTO.getNotes());
        order.setAssignedTo(assignedTo);

        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Transactional
    public OrderDTO cancelOrder(Long id, String cancellationReason) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        // Check if order can be cancelled (not already delivered or cancelled)
        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel an already delivered order");
        }
        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }

        // Save the previous status for notification
        Order.OrderStatus previousStatus = order.getStatus();

        // Update order status
        order.setStatus(Order.OrderStatus.CANCELLED);

        // Add cancellation reason to notes
        if (cancellationReason != null && !cancellationReason.isEmpty()) {
            String notes = order.getNotes() != null ? order.getNotes() + "\n\n" : "";
            order.setNotes(notes + "Cancellation Reason: " + cancellationReason);
        }

        // Set cancellation time
        order.setCancellationDate(LocalDateTime.now());

        Order cancelledOrder = orderRepository.save(order);

        // Create notification
        String message = "Order " + order.getOrderNumber() + " has been cancelled. " +
                (cancellationReason != null ? "Reason: " + cancellationReason : "");
        notificationService.createOrderNotification(cancelledOrder, message);

        return convertToDTO(cancelledOrder);
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setCustomerName(order.getCustomer().getName());
        dto.setOrderDate(order.getOrderDate());
        dto.setDeliveryDate(order.getDeliveryDate());
        dto.setStatus(order.getStatus().name());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setNotes(order.getNotes());
        
        if (order.getCreatedBy() != null) {
            dto.setCreatedAt(order.getCreatedAt());
            //dto.setCreatedByName(order.getCreatedBy().getFullName());
        }
        
        if (order.getAssignedTo() != null) {
            dto.setAssignedTo(String.valueOf(order.getAssignedTo().getId()));
            dto.setAssignedTo(order.getAssignedTo().getFullName());
        }
        
        List<OrderItemDTO> items = order.getItems().stream()
                .map(item -> {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    itemDTO.setId(item.getId());
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setProductName(item.getProduct().getName());
                    itemDTO.setProductSku(item.getProduct().getSku());
                    itemDTO.setQuantity(Long.valueOf(item.getQuantity()));
                    itemDTO.setUnitPrice(item.getPrice());
                    itemDTO.setSubtotal(item.getSubtotal());
                    return itemDTO;
                })
                .collect(Collectors.toList());
        
        dto.setOrderItems(items);
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        return dto;
    }
}
