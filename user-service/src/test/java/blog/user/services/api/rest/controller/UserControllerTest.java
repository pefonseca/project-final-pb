package blog.user.services.api.rest.controller;

import blog.user.services.api.rest.dto.request.UserCreateRequestDTO;
import blog.user.services.api.rest.dto.request.UserUpdateRequestDTO;
import blog.user.services.api.rest.dto.response.UserResponseDTO;
import blog.user.services.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByEmail_ShouldReturnUserResponseDTO() {
        String email = "test@example.com";
        UserResponseDTO userResponseDTO = UserResponseDTO.builder().id(1L).email(email).build();
        when(userService.findByEmail(email)).thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> response = userController.findByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody());
        verify(userService, times(1)).findByEmail(email);
    }

    @Test
    void findById_ShouldReturnUserResponseDTO() {
        Long userId = 1L;
        UserResponseDTO userResponseDTO = UserResponseDTO.builder().id(userId).email("test@example.com").build();
        when(userService.findById(userId)).thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> response = userController.findById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody());
        verify(userService, times(1)).findById(userId);
    }

    @Test
    void create_ShouldReturnCreatedUserResponseDTO() {
        UserCreateRequestDTO createRequestDTO = UserCreateRequestDTO.builder().email("newuser@example.com").build();
        UserResponseDTO userResponseDTO = UserResponseDTO.builder().id(2L).email("newuser@example.com").build();
        when(userService.create(createRequestDTO)).thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> response = userController.create(createRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody());
        verify(userService, times(1)).create(createRequestDTO);
    }

    @Test
    void update_ShouldReturnUpdatedUserResponseDTO() {
        Long userId = 1L;
        UserUpdateRequestDTO updateRequestDTO = UserUpdateRequestDTO.builder().email("updateduser@example.com").build();
        UserResponseDTO userResponseDTO = UserResponseDTO.builder().id(userId).email("updateduser@example.com").build();
        when(userService.update(updateRequestDTO, userId)).thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> response = userController.update(updateRequestDTO, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody());
        verify(userService, times(1)).update(updateRequestDTO, userId);
    }
}
