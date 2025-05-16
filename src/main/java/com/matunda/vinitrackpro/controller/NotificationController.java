package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.NotificationDTO;
import com.matunda.vinitrackpro.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getCurrentUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getCurrentUserNotifications(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationDTO>> getCurrentUserUnreadNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getCurrentUserUnreadNotifications(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationDTO> createNotification(@Valid @RequestBody NotificationDTO notificationDTO) {
        return new ResponseEntity<>(notificationService.createNotification(notificationDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markNotificationAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markNotificationAsRead(id));
    }


    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllCurrentUserNotificationsAsRead(@PathVariable Long userId) {
        notificationService.markAllCurrentUserNotificationsAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
