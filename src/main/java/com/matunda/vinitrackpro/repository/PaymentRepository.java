package com.matunda.vinitrackpro.repository;
import com.matunda.vinitrackpro.model.Order;
import com.matunda.vinitrackpro.model.Payment;
import com.matunda.vinitrackpro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByOrder(Order order);
    List<Payment> findByStatus(Payment.PaymentStatus status);
    List<Payment> findByMethod(Payment.PaymentMethod method);
    List<Payment> findByProcessedBy(User user);
    List<Payment> findByPaymentDateBetween(LocalDateTime start, LocalDateTime end);

}
