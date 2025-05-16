package com.matunda.vinitrackpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private Long id;
    
    private Long userId;
    
    private String title;
    
    private String message;
    
    private String notificationType;
    
    private boolean read;

    private String actionUrl;
    
    private String link;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime readAt;
}
