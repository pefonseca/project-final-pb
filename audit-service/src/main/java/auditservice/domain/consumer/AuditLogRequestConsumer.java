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
        log.info("[AuditLogServiceImpl] -> (logAudit): recebendo a mensagem para salvar a alteração na entidade.");
        var messageRequest = message.getPayload();

        log.debug("[AuditLogRequestConsumer] -> (logAudit): Payload recebido: {}", messageRequest);

        log.info("[AuditLogRequestConsumer] -> (logAudit): Validando mensagem recebida.");
        if (messageRequest instanceof String) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                AuditMessageDTO auditMessageDTO = objectMapper.readValue((String) messageRequest, AuditMessageDTO.class);

                log.info("[AuditLogRequestConsumer] -> (logAudit): Mensagem desserializada com sucesso.");

                AuditLog auditLog = new AuditLog(
                        null,
                        auditMessageDTO.getEntityName(),
                        auditMessageDTO.getEntityId(),
                        auditMessageDTO.getAction(),
                        LocalDateTime.now(),
                        auditMessageDTO.getDetails()
                );
                log.debug("[AuditLogRequestConsumer] -> (logAudit): Salvando o log de auditoria: {}", auditLog);
                auditLogRepository.save(auditLog);

                log.info("[AuditLogRequestConsumer] -> (logAudit): Log de auditoria salvo com sucesso.");
            } catch (JsonProcessingException e) {
                log.error("[AuditLogServiceImpl] -> (logAudit): Erro ao desserializar a mensagem: " + e.getMessage());
            }
        } else {
            log.error("[AuditLogServiceImpl] -> (logAudit): Tipo de payload inesperado: " + messageRequest.getClass().getName());
        }
    }
}
