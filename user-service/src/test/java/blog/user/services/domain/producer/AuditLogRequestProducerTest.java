package blog.user.services.domain.producer;

import blog.user.services.domain.model.dto.AuditMessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.AmqpTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuditLogRequestProducerTest {

    @Mock
    private AmqpTemplate amqpTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuditLogRequestProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void integration_ShouldSendAuditMessageSuccessfully() throws JsonProcessingException {
        AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder().entityName("COMMENT").entityId(2L).action("CREATE").performedBy("User Id:").details("Comentário criado com sucesso.").build();
        String message = "mockedMessage";
        when(objectMapper.writeValueAsString(auditMessageDTO)).thenReturn(message);
        producer.integration(auditMessageDTO);
        assertDoesNotThrow(() -> producer.integration(auditMessageDTO));
    }

    @Test
    void integration_ShouldLogError_WhenJsonProcessingExceptionOccurs() throws JsonProcessingException {
        AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder().entityName("COMMENT").entityId(2L).action("CREATE").performedBy("User Id:").details("Comentário criado com sucesso.").build();
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        assertDoesNotThrow(() -> producer.integration(auditMessageDTO));
    }

}