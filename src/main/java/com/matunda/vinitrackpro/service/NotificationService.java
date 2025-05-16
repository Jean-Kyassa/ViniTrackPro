package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.NotificationDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.*;
import com.matunda.vinitrackpro.repository.NotificationRepository;
import com.matunda.vinitrackpro.repository.RoleRepository;
import com.matunda.vinitrackpro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<NotificationDTO> getNotificationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return notificationRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getUnreadNotificationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return notificationRepository.findByUserAndReadFalse(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        User user = userRepository.findById(notificationDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + notificationDTO.getUserId()));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(notificationDTO.getTitle());
        notification.setMessage(notificationDTO.getMessage());
        notification.setType(Notification.NotificationType.valueOf(notificationDTO.getNotificationType()));
        notification.setRead(notificationDTO.isRead());
        notification.setActionUrl(notificationDTO.getActionUrl());

        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    @Transactional
    public void createLowStockNotification(Inventory inventory) {
        // Find users with inventory management roles
        Role industryManagerRole = roleRepository.findByName(Role.RoleType.ROLE_INDUSTRY_MANAGER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        Role accountantRole = roleRepository.findByName(Role.RoleType.ROLE_ACCOUNTANT)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(industryManagerRole) || 
                                user.getRoles().contains(accountantRole))
                .collect(Collectors.toList());
        
        String title = "Low Stock Alert";
        String message = "Product " + inventory.getProduct().getName() + " (SKU: " + 
                inventory.getProduct().getSku() + ") is running low. Current quantity: " + 
                inventory.getQuantity() + ", Minimum level: " + inventory.getMinimumStockLevel();
        
        for (User user : users) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setType(Notification.NotificationType.INVENTORY_ALERT);
            notification.setRead(false);
            notification.setActionUrl("/inventory/" + inventory.getId());
            
            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void createOrderNotification(Order order, String message) {
        // Find logistics and distribution managers
        Role logisticsManagerRole = roleRepository.findByName(Role.RoleType.ROLE_LOGISTICS_MANAGER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        Role distributionManagerRole = roleRepository.findByName(Role.RoleType.ROLE_DISTRIBUTION_MANAGER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(logisticsManagerRole) || 
                                user.getRoles().contains(distributionManagerRole))
                .collect(Collectors.toList());
        
        // Also notify the assigned user if any
        if (order.getAssignedTo() != null && !users.contains(order.getAssignedTo())) {
            users.add(order.getAssignedTo());
        }
        
        String title = "Order Update: " + order.getOrderNumber();
        
        for (User user : users) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setType(Notification.NotificationType.ORDER_UPDATE);
            notification.setRead(false);
            notification.setActionUrl("/orders/" + order.getId());
            
            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void createQualityControlNotification(QualityControl qc, String message) {
        // Find industry manager and quality control users
        Role industryManagerRole = roleRepository.findByName(Role.RoleType.ROLE_INDUSTRY_MANAGER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        Role qualityControlRole = roleRepository.findByName(Role.RoleType.ROLE_QUALITY_CONTROL)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(industryManagerRole) || 
                                user.getRoles().contains(qualityControlRole))
                .collect(Collectors.toList());
        
        String title = "Quality Control Update: " + qc.getBatchNumber();
        
        for (User user : users) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setType(Notification.NotificationType.QUALITY_CONTROL);
            notification.setRead(false);
            notification.setActionUrl("/quality-control/" + qc.getId());
            
            notificationRepository.save(notification);
        }
    }

    // New methods to match controller requirements
    public List<NotificationDTO> getCurrentUserNotifications(Long userId) {
        return getNotificationsByUser(userId);
    }
    public List<NotificationDTO> getCurrentUserUnreadNotifications(Long userId) {
        return getUnreadNotificationsByUser(userId);
    }

    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        return convertToDTO(notification);
    }

    @Transactional
    public NotificationDTO markNotificationAsRead(Long id) {
        return markAsRead(id);
    }

    @Transactional
    public void markAllCurrentUserNotificationsAsRead(Long userId) {
        markAllAsRead(userId);
    }
    @Transactional
    public NotificationDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        
        notification.setRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return convertToDTO(updatedNotification);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        List<Notification> unreadNotifications = notificationRepository.findByUserAndReadFalse(user);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    @Transactional
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUser().getId());
        //dto.setUserName(notification.getUser().getFullName());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setNotificationType(notification.getType().name());
        dto.setRead(notification.isRead());
        dto.setActionUrl(notification.getActionUrl());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
