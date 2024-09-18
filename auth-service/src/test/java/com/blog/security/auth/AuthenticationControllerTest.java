package com.blog.security.auth;

import com.blog.security.infra.feign.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_ShouldReturnOk_WhenUserIsRegistered() {
        RegisterRequest request = new RegisterRequest("name", "name", "email@email", "123", "test@test.com", "password");
        AuthenticationResponse response = new AuthenticationResponse("token123");
        when(authenticationService.register(request)).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = authenticationController.register(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authenticationService, times(1)).register(request);
    }

    @Test
    void authenticate_ShouldReturnOk_WhenUserIsAuthenticated() {
        AuthenticationRequest request = new AuthenticationRequest("test@test.com", "password");
        AuthenticationResponse response = new AuthenticationResponse("token123");
        when(authenticationService.authenticate(request)).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = authenticationController.authenticate(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authenticationService, times(1)).authenticate(request);
    }

    @Test
    void getUserProfile_ShouldReturnOk_WhenUserProfileIsRequested() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@test.com");
        UserResponse userResponse = new UserResponse();
        when(authenticationService.findUserByEmail("test@test.com")).thenReturn(userResponse);

        ResponseEntity<UserResponse> result = authenticationController.getUserProfile(userDetails);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userResponse, result.getBody());
        verify(authenticationService, times(1)).findUserByEmail("test@test.com");
    }
}