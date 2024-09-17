package auditservice.infra.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String AUDIT_LOG_REQUEST_QUEUE = "audit-log-request-queue";

    public Queue auditLogRequestQueue() {
        return new Queue(AUDIT_LOG_REQUEST_QUEUE);
    }
}
