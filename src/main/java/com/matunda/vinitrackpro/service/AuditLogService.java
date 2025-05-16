package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.AuditLogDTO;
import com.matunda.vinitrackpro.model.AuditLog;
import com.matunda.vinitrackpro.model.User;
import com.matunda.vinitrackpro.repository.AuditLogRepository;
import com.matunda.vinitrackpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public void logActivity(String action, String entityType, Long entityId, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setDetails(details);
        
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
                !authentication.getName().equals("anonymousUser")) {
            userRepository.findByUsername(authentication.getName())
                    .ifPresent(auditLog::setUser);
        }
        
        auditLogRepository.save(auditLog);
    }

    public Page<AuditLogDTO> getAuditLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public List<AuditLogDTO> getAuditLogsByUser(Long userId) {
        return auditLogRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getAuditLogsByEntityType(String entityType) {
        return auditLogRepository.findByEntityType(entityType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getAuditLogsByEntityId(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AuditLogDTO convertToDTO(AuditLog auditLog) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(auditLog.getId());
        dto.setAction(auditLog.getAction());
        dto.setEntityType(auditLog.getEntityType());
        dto.setEntityId(auditLog.getEntityId());
        dto.setTimestamp(auditLog.getTimestamp());
        dto.setDetails(auditLog.getDetails());
        
        User user = auditLog.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setUserFullName(user.getFullName());
        }
        
        return dto;
    }
}
