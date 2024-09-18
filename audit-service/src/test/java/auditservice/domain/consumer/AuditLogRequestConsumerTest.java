package auditservice.domain.consumer;

import auditservice.domain.entity.AuditLog;
import auditservice.domain.model.AuditMessageDTO;
import auditservice.infra.repository.AuditLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AuditLogRequestConsumerTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogRequestConsumer auditLogRequestConsumer;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void logAudit_validMessage_savesAuditLog() throws JsonProcessingException {
        AuditMessageDTO auditMessageDTO = new AuditMessageDTO(
                "User",
                1L,
                "CREATE",
                "User created",
                "User created"
        );
        String jsonMessage = objectMapper.writeValueAsString(auditMessageDTO);
        Message<String> message = MessageBuilder.withPayload(jsonMessage).build();

        auditLogRequestConsumer.logAudit(message);

        ArgumentCaptor<AuditLog> auditLogCaptor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository, times(1)).save(auditLogCaptor.capture());

        AuditLog savedAuditLog = auditLogCaptor.getValue();
        assertNotNull(savedAuditLog);
        assertEquals("User", savedAuditLog.getEntityName());
        assertEquals(1L, savedAuditLog.getEntityId());
        assertEquals("CREATE", savedAuditLog.getAction());
        assertEquals("User created", savedAuditLog.getDetails());
        assertNotNull(savedAuditLog.getTimestamp());
    }

    @Test
    void logAudit_invalidJson_doesNotSaveAuditLog() {
        String invalidJsonMessage = "invalid json";
        Message<String> message = MessageBuilder.withPayload(invalidJsonMessage).build();

        auditLogRequestConsumer.logAudit(message);

        verify(auditLogRepository, never()).save(any(AuditLog.class));
    }

    @Test
    void logAudit_unexpectedPayloadType_logsError() {
        Message<Integer> message = MessageBuilder.withPayload(123).build();

        auditLogRequestConsumer.logAudit(message);

        verify(auditLogRepository, never()).save(any(AuditLog.class));
    }
}