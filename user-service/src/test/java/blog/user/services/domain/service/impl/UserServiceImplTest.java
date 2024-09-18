package blog.user.services.domain.service.impl;

import blog.user.services.api.rest.dto.request.UserCreateRequestDTO;
import blog.user.services.api.rest.dto.request.UserUpdateRequestDTO;
import blog.user.services.api.rest.dto.response.UserResponseDTO;
import blog.user.services.domain.model.dto.AuditMessageDTO;
import blog.user.services.domain.model.entity.User;
import blog.user.services.domain.producer.AuditLogRequestProducer;
import blog.user.services.infra.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private AuditLogRequestProducer producer;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_ShouldReturnUserResponseDTO() {
        Long userId = 1L;
        when(repository.findById(userId)).thenReturn(Optional.of(User.builder().id(1L).updateDate(LocalDateTime.now()).build()));

        UserResponseDTO response = userService.findById(userId);

        assertNotNull(response);
        assertEquals(userId, response.getId());
    }

    @Test
    void findById_ShouldThrowExceptionWhenUserNotFound() {
        Long userId = 1L;
        when(repository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> userService.findById(userId));
        assertEquals("Usuário não encontrado.", thrown.getMessage());
    }

    @Test
    void findByEmail_ShouldThrowExceptionWhenUserNotFound() {
        String email = "test@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> userService.findByEmail(email));
        assertEquals("Usuário não encontrado.", thrown.getMessage());
    }
}
