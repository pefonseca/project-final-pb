package blog.user.services.domain.producer;

import blog.user.services.domain.model.dto.AuditMessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static blog.user.services.infra.config.audit.AuditLogExchangeConfig.EXCHANGE_NAME;
import static blog.user.services.infra.config.audit.AuditLogRabbitConfig.ROUTING_KEY_NAME;

@Slf4j
@Component
public class AuditLogRequestProducer {

    @Autowired private AmqpTemplate amqpTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void integration(AuditMessageDTO auditMessageDTO) {
        try {
            log.info("[AuditLogRequestProducer] -> (integration): Enviando mensagem de auditoria para Exchange: {} com Routing Key: {}", EXCHANGE_NAME, ROUTING_KEY_NAME);
            String message = objectMapper.writeValueAsString(auditMessageDTO);
            amqpTemplate.convertAndSend(
                    EXCHANGE_NAME,
                    ROUTING_KEY_NAME,
                    message
            );
            log.info("[AuditLogRequestProducer] -> (integration): Mensagem de auditoria enviada com sucesso: {}", message);
        } catch (JsonProcessingException e) {
            log.error("[AuditLogRequestProducer] -> (integration): Ocorreu um erro ao integrar com a aplicação de auditoria: {}", e.getMessage());
        }
    }
}
