package auditservice.domain.service;

import auditservice.domain.entity.AuditLog;

import java.util.List;

public interface AuditLogService {

    List<AuditLog> getAuditLogs();

}
