package com.adminmodule.backend.service;

import com.adminmodule.backend.entity.AuditLog;
import com.adminmodule.backend.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    
    private final AuditLogRepository auditLogRepository;

    // Hàm dùng chung để ghi lại lịch sử thao tác
    public void logAction(UUID userId, String actionType, String entityName, String ipAddress) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setActionType(actionType);
        log.setEntityName(entityName);
        log.setIpAddress(ipAddress);
        
        auditLogRepository.save(log);
    }
}