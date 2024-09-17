package blog.user.services.infra.config.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AuditLogExchangeConfig {

    public static final String EXCHANGE_NAME = "audit-log-exchange";

    @Bean
    public DirectExchange exchange() {
        log.info("[AuditLogExchangeConfig] -> (exchange): Criando DirectExchange com nome: {}", EXCHANGE_NAME);
        return new DirectExchange(EXCHANGE_NAME);
    }

}
