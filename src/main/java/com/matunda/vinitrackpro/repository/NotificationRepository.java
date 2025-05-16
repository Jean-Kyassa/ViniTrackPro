package com.matunda.vinitrackpro.repository;
import com.matunda.vinitrackpro.model.Notification;
import com.matunda.vinitrackpro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndReadFalse(User user);
    List<Notification> findByUserAndType(User user, Notification.NotificationType type);
    List<Notification> findByCreatedAtAfter(LocalDateTime dateTime);
    List<Notification> findByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);
}
