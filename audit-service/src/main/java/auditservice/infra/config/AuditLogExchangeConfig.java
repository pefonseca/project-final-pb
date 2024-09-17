package auditservice.infra.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AuditLogExchangeConfig {

    public static final String EXCHANGE_NAME = "audit-log-exchange";

    @Bean
    DirectExchange exchange() {
        log.info("[AuditLogExchangeConfig] -> (exchange): Configurando o DirectExchange com o nome '{}'.", EXCHANGE_NAME);
        log.info("[AuditLogExchangeConfig] -> (exchange): DirectExchange '{}' configurado com sucesso.", EXCHANGE_NAME);
        return new DirectExchange(EXCHANGE_NAME);
    }

}
