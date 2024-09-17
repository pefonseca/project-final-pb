package com.blog.security.post.services.domain.producer;


import com.blog.security.post.services.domain.model.dto.AuditMessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.blog.security.post.services.infra.config.audit.AuditLogExchangeConfig.EXCHANGE_NAME;
import static com.blog.security.post.services.infra.config.audit.AuditLogRabbitConfig.ROUTING_KEY_NAME;


@Slf4j
@Component
public class AuditLogRequestProducer {

    @Autowired private AmqpTemplate amqpTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void integration(AuditMessageDTO auditMessageDTO) {
        try {
            amqpTemplate.convertAndSend(
                    EXCHANGE_NAME,
                    ROUTING_KEY_NAME,
                    objectMapper.writeValueAsString(auditMessageDTO)
            );
        } catch (JsonProcessingException e) {
            log.error("Ocorreu um erro ao integrar com a aplicação de auditoria: {}", e.getMessage());
        }
    }
}
