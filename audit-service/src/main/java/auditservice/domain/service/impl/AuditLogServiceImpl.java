package auditservice.domain.service.impl;

import auditservice.domain.entity.AuditLog;
import auditservice.domain.service.AuditLogService;
import auditservice.infra.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public List<AuditLog> getAuditLogs() {
        return auditLogRepository.findAll();
    }
}