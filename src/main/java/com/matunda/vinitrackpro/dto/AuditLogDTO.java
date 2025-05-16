package com.matunda.vinitrackpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO {

    private Long id;
    
    private String action;
    
    private String entityType;
    
    private Long entityId;
    
    private String details;
    
    private Long userId;
    
    private String username;

    private String userFullName;
    
    private String ipAddress;
    
    private LocalDateTime timestamp;
}
