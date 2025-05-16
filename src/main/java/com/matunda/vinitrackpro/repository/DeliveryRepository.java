package com.matunda.vinitrackpro.repository;
import com.matunda.vinitrackpro.model.Delivery;
import com.matunda.vinitrackpro.model.Order;
import com.matunda.vinitrackpro.model.User;
import com.matunda.vinitrackpro.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByTrackingNumber(String trackingNumber);
    List<Delivery> findByOrder(Order order);
    List<Delivery> findByDriver(User driver);
    List<Delivery> findByVehicle(Vehicle vehicle);
    List<Delivery> findByStatus(Delivery.DeliveryStatus status);
    List<Delivery> findByScheduledDateBetween(LocalDateTime start, LocalDateTime end);
}

