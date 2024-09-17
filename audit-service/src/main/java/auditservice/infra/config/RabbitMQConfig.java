package auditservice.infra.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig {

    public static final String AUDIT_LOG_REQUEST_QUEUE = "audit-log-request-queue";

    public Queue auditLogRequestQueue() {
        log.info("[RabbitMQConfig] -> (auditLogRequestQueue): Configurando a fila '{}'.", AUDIT_LOG_REQUEST_QUEUE);
        log.info("[RabbitMQConfig] -> (auditLogRequestQueue): Fila '{}' configurada com sucesso.", AUDIT_LOG_REQUEST_QUEUE);
        return new Queue(AUDIT_LOG_REQUEST_QUEUE);
    }
}
