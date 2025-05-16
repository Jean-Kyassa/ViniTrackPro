//package com.matunda.vinitrackpro.service;
//import com.matunda.vinitrackpro.dto.PaymentDTO;
//import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
//import com.matunda.vinitrackpro.model.Order;
//import com.matunda.vinitrackpro.model.Payment;
//import com.matunda.vinitrackpro.model.User;
//import com.matunda.vinitrackpro.repository.OrderRepository;
//import com.matunda.vinitrackpro.repository.PaymentRepository;
//import com.matunda.vinitrackpro.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class PaymentService {
//
//    private final PaymentRepository paymentRepository;
//    private final OrderRepository orderRepository;
//    private final UserRepository userRepository;
//    private final NotificationService notificationService;
//    private final OrderService orderService;
//
//    public List<PaymentDTO> getAllPayments() {
//        return paymentRepository.findAll().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    public PaymentDTO getPaymentById(Long id) {
//        Payment payment = paymentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
//        return convertToDTO(payment);
//    }
//
//    public PaymentDTO getPaymentByTransactionId(String transactionId) {
//        Payment payment = paymentRepository.findByTransactionId(transactionId)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with transaction id: " + transactionId));
//        return convertToDTO(payment);
//    }
//
//    public List<PaymentDTO> getPaymentsByOrder(Long orderId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
//        return paymentRepository.findByOrder(order).stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    public List<PaymentDTO> getPaymentsByStatus(String status) {
//        Payment.PaymentStatus paymentStatus = Payment.PaymentStatus.valueOf(status);
//        return paymentRepository.findByStatus(paymentStatus).stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
//        Order order = orderRepository.findById(paymentDTO.getOrderId())
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + paymentDTO.getOrderId()));
//
//        User processedBy = null;
//        if (paymentDTO.getProcessedById() != null) {
//            processedBy = userRepository.findAll(paymentDTO.getProcessedById())
//                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + paymentDTO.getProcessedById()));
//        }
//
//        Payment payment = new Payment();
//        payment.setTransactionId(generateTransactionId());
//        payment.setOrder(order);
//        payment.setAmount(paymentDTO.getAmount());
//        payment.setPaymentDate(LocalDateTime.now());
//        payment.setMethod(Payment.PaymentMethod.valueOf(paymentDTO.getPaymentMethod()));
//        payment.setStatus(Payment.PaymentStatus.valueOf(paymentDTO.getStatus()));
//        payment.setReceiptUrl(paymentDTO.getReceiptUrl());
//        payment.setNotes(paymentDTO.getNotes());
//        payment.setProcessedBy(processedBy);
//
//        Payment savedPayment = paymentRepository.save(payment);
//
//        // If payment is completed, update order status to PAID
//        if (savedPayment.getStatus() == Payment.PaymentStatus.COMPLETED) {
//            orderService.updateOrderStatus(order
//            .getId(), Order.OrderStatus.PAID.name());
//        }
//
//        // Create notification for new payment
//        String message = "New payment of " + paymentDTO.getAmount() + " received for order " +
//                         order.getOrderNumber() + " via " + paymentDTO.getPaymentMethod();
//        notificationService.createOrderNotification(order, message);
//
//        return convertToDTO(savedPayment);
//    }
//
//    @Transactional
//    public PaymentDTO updatePaymentStatus(Long id, String status, String notes) {
//        Payment payment = paymentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
//
//        Payment.PaymentStatus newStatus = Payment.PaymentStatus.valueOf(status);
//        Payment.PaymentStatus oldStatus = payment.getStatus();
//
//        payment.setStatus(newStatus);
//
//        if (notes != null && !notes.isEmpty()) {
//            payment.setNotes(notes);
//        }
//
//        Payment updatedPayment = paymentRepository.save(payment);
//
//        // If payment is now completed, update order status to PAID
//        if (newStatus == Payment.PaymentStatus.COMPLETED && oldStatus != Payment.PaymentStatus.COMPLETED) {
//            orderService.updateOrderStatus(payment.getOrder().getId(), Order.OrderStatus.PAID.name());
//        }
//
//        // Create notification for status change
//        String message = "Payment status changed from " + oldStatus + " to " + newStatus +
//                         " for order " + payment.getOrder().getOrderNumber();
//        notificationService.createOrderNotification(payment.getOrder(), message);
//
//        return convertToDTO(updatedPayment);
//    }
//
//    @Transactional
//    public PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO) {
//        Payment payment = paymentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
//
//        User processedBy = null;
//        if (paymentDTO.getProcessedById() != null) {
//            processedBy = userRepository.findById(Long.parseLong(paymentDTO.getProcessedById()))
//                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + paymentDTO.getProcessedById()));
//        }
//
//        payment.setAmount(paymentDTO.getAmount());
//        payment.setMethod(Payment.PaymentMethod.valueOf(paymentDTO.getPaymentMethod()));
//        payment.setStatus(Payment.PaymentStatus.valueOf(paymentDTO.getStatus()));
//        payment.setReceiptUrl(paymentDTO.getReceiptUrl());
//        payment.setNotes(paymentDTO.getNotes());
//        payment.setProcessedBy(processedBy);
//
//        Payment updatedPayment = paymentRepository.save(payment);
//        return convertToDTO(updatedPayment);
//    }
//
//    @Transactional
//    public void deletePayment(Long id) {
//        if (!paymentRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Payment not found with id: " + id);
//        }
//        paymentRepository.deleteById(id);
//    }
//
//    private String generateTransactionId() {
//        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
//    }
//
//    private PaymentDTO convertToDTO(Payment payment) {
//        PaymentDTO dto = new PaymentDTO();
//        dto.setId(payment.getId());
//        dto.setTransactionId(payment.getTransactionId());
//        dto.setOrderId(payment.getOrder().getId());
//        dto.setOrderNumber(payment.getOrder().getOrderNumber());
//        dto.setAmount(payment.getAmount());
//        dto.setPaymentDate(payment.getPaymentDate());
//        dto.setPaymentMethod(payment.getMethod().name());
//        dto.setStatus(payment.getStatus().name());
//        dto.setReceiptUrl(payment.getReceiptUrl());
//        dto.setNotes(payment.getNotes());
//
//        if (payment.getProcessedBy() != null) {
//            dto.setProcessedBy(String.valueOf(payment.getProcessedBy().getId()));
//            dto.setProcessedBy(payment.getProcessedBy().getFullName());
//        }
//
//        dto.setCreatedAt(payment.getCreatedAt());
//        dto.setUpdatedAt(payment.getUpdatedAt());
//
//        return dto;
//    }
//}

package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.PaymentDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.Order;
import com.matunda.vinitrackpro.model.Payment;
import com.matunda.vinitrackpro.model.User;
import com.matunda.vinitrackpro.repository.OrderRepository;
import com.matunda.vinitrackpro.repository.PaymentRepository;
import com.matunda.vinitrackpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final OrderService orderService;

    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return convertToDTO(payment);
    }

    public PaymentDTO getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with transaction id: " + transactionId));
        return convertToDTO(payment);
    }

    public List<PaymentDTO> getPaymentsByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return paymentRepository.findByOrder(order).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getPaymentsByStatus(String status) {
        Payment.PaymentStatus paymentStatus = Payment.PaymentStatus.valueOf(status);
        return paymentRepository.findByStatus(paymentStatus).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Order order = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + paymentDTO.getOrderId()));

        User processedBy = null;
        if (paymentDTO.getProcessedBy() != null && !paymentDTO.getProcessedBy().isEmpty()) {
            processedBy = userRepository.findById(Long.valueOf(paymentDTO.getProcessedBy()))
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + paymentDTO.getProcessedBy()));
        }

        Payment payment = new Payment();
        payment.setTransactionId(generateTransactionId());
        payment.setOrder(order);
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setMethod(Payment.PaymentMethod.valueOf(paymentDTO.getPaymentMethod()));
        payment.setStatus(Payment.PaymentStatus.valueOf(paymentDTO.getStatus()));
        payment.setReceiptUrl(paymentDTO.getReceiptUrl());
        payment.setNotes(paymentDTO.getNotes());
        payment.setProcessedBy(processedBy);

        Payment savedPayment = paymentRepository.save(payment);

        // If payment is completed, update order status to PAID
        if (savedPayment.getStatus() == Payment.PaymentStatus.COMPLETED) {
            orderService.updateOrderStatus(order.getId(), Order.OrderStatus.PAID.name());
        }

        // Create notification for new payment
        String message = "New payment of " + paymentDTO.getAmount() + " received for order " +
                order.getOrderNumber() + " via " + paymentDTO.getPaymentMethod();
        notificationService.createOrderNotification(order, message);

        return convertToDTO(savedPayment);
    }

    @Transactional
    public PaymentDTO updatePaymentStatus(Long id, String status, String notes) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        Payment.PaymentStatus newStatus = Payment.PaymentStatus.valueOf(status);
        Payment.PaymentStatus oldStatus = payment.getStatus();

        payment.setStatus(newStatus);

        if (notes != null && !notes.isEmpty()) {
            payment.setNotes(notes);
        }

        Payment updatedPayment = paymentRepository.save(payment);

        // If payment is now completed, update order status to PAID
        if (newStatus == Payment.PaymentStatus.COMPLETED && oldStatus != Payment.PaymentStatus.COMPLETED) {
            orderService.updateOrderStatus(payment.getOrder().getId(), Order.OrderStatus.PAID.name());
        }

        // Create notification for status change
        String message = "Payment status changed from " + oldStatus + " to " + newStatus +
                " for order " + payment.getOrder().getOrderNumber();
        notificationService.createOrderNotification(payment.getOrder(), message);

        return convertToDTO(updatedPayment);
    }

    @Transactional
    public PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        User processedBy = null;
        if (paymentDTO.getProcessedBy() != null && !paymentDTO.getProcessedBy().isEmpty()) {
            processedBy = userRepository.findById(Long.valueOf(paymentDTO.getProcessedBy()))
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + paymentDTO.getProcessedBy()));
        }

        payment.setAmount(paymentDTO.getAmount());
        payment.setMethod(Payment.PaymentMethod.valueOf(paymentDTO.getPaymentMethod()));
        payment.setStatus(Payment.PaymentStatus.valueOf(paymentDTO.getStatus()));
        payment.setReceiptUrl(paymentDTO.getReceiptUrl());
        payment.setNotes(paymentDTO.getNotes());
        payment.setProcessedBy(processedBy);

        Payment updatedPayment = paymentRepository.save(payment);
        return convertToDTO(updatedPayment);
    }

    @Transactional
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setTransactionId(payment.getTransactionId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setOrderNumber(payment.getOrder().getOrderNumber());
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setPaymentMethod(payment.getMethod().name());
        dto.setStatus(payment.getStatus().name());
        dto.setReceiptUrl(payment.getReceiptUrl());
        dto.setNotes(payment.getNotes());

        if (payment.getProcessedBy() != null) {
            // Assuming you want the full name rather than the ID since that was last in the original code
            dto.setProcessedBy(payment.getProcessedBy().getFullName());
        }

        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());

        return dto;
    }
}