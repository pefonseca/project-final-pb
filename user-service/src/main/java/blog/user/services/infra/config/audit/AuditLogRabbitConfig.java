package blog.user.services.infra.config.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuditLogRabbitConfig {

    private final DirectExchange directExchange;

    public static final String ROUTING_KEY_NAME = "audit-log-route";
    public static final String QUEUE_NAME = "audit-log-queue";

    @Bean
    public Queue queue() {
        log.info("[AuditLogRabbitConfig] -> (queue): Criando fila com nome: {}", QUEUE_NAME);
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    Binding binding() {
        log.info("[AuditLogRabbitConfig] -> (binding): Criando binding entre fila '{}' e exchange '{}' com routing key '{}'",
                QUEUE_NAME, directExchange.getName(), ROUTING_KEY_NAME);
        return BindingBuilder.bind(queue()).to(directExchange).with(ROUTING_KEY_NAME);
    }

}
