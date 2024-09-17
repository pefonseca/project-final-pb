package auditservice.domain.consumer;

import auditservice.domain.entity.AuditLog;
import auditservice.domain.model.AuditMessageDTO;
import auditservice.infra.repository.AuditLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AuditLogRequestConsumer {

    @Autowired private AuditLogRepository auditLogRepository;

    @RabbitListener(queues = "audit-log-queue")
    public void logAudit(@Payload Message message) {
        var messageRequest = message.getPayload();

        if (messageRequest instanceof String) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                AuditMessageDTO auditMessageDTO = objectMapper.readValue((String) messageRequest, AuditMessageDTO.class);

                AuditLog log = new AuditLog(
                        null,
                        auditMessageDTO.getEntityName(),
                        auditMessageDTO.getEntityId(),
                        auditMessageDTO.getAction(),
                        LocalDateTime.now(),
                        auditMessageDTO.getDetails()
                );

                auditLogRepository.save(log);
            } catch (JsonProcessingException e) {
                log.error("[AuditLogServiceImpl] -> (logAudit): Erro ao desserializar a mensagem: " + e.getMessage());
            }
        } else {
            log.error("[AuditLogServiceImpl] -> (logAudit): Tipo de payload inesperado: " + messageRequest.getClass().getName());
        }
    }
}
