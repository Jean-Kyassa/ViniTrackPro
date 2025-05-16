package com.matunda.vinitrackpro.repository;
import com.matunda.vinitrackpro.model.Customer;
import com.matunda.vinitrackpro.model.Order;
import com.matunda.vinitrackpro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByCustomer(Customer customer);
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByCreatedBy(User user);
    List<Order> findByAssignedTo(User user);
    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
    List<Order> findByStatusAndDeliveryDateBetween(Order.OrderStatus status, LocalDateTime start, LocalDateTime end);
}
