package auditservice.domain.service.impl;

import auditservice.domain.entity.AuditLog;
import auditservice.domain.service.AuditLogService;
import auditservice.infra.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public List<AuditLog> getAuditLogs() {
        return auditLogRepository.findAll();
    }
}
