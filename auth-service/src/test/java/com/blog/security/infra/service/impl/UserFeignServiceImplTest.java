package com.blog.security.infra.service.impl;

import com.blog.security.infra.feign.UserFeignClient;
import com.blog.security.infra.feign.response.UserResponse;
import com.blog.security.infra.request.UserRequest;
import com.blog.security.infra.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserFeignServiceImplTest {

    @Mock
    private UserFeignClient userFeignClient;

    @InjectMocks
    private UserFeignServiceImpl userFeignServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldReturnUserResponse_WhenUserIsCreatedSuccessfully() {
        UserRequest request = new UserRequest("", "", "", "", "", "", "test@test.com", "Test", "password", Role.USER);
        UserResponse expectedResponse = new UserResponse(1L, "", "", "", "", "", "", "test@test.com", "Test", "password", "");

        when(userFeignClient.create(request)).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.CREATED));

        UserResponse actualResponse = userFeignServiceImpl.create(request);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getEmail(), actualResponse.getEmail());
        verify(userFeignClient, times(1)).create(request);
    }

    @Test
    void create_ShouldReturnEmptyUserResponse_WhenUserCreationFails() {
        // Arrange
        UserRequest request = new UserRequest("", "", "", "", "", "", "test@test.com", "Test", "password", Role.USER);

        when(userFeignClient.create(request)).thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

        // Act
        UserResponse actualResponse = userFeignServiceImpl.create(request);

        // Assert
        assertNull(actualResponse.getId());
        verify(userFeignClient, times(1)).create(request);
    }

    @Test
    void findByEmail_ShouldReturnUserResponse_WhenUserIsFound() {
        String email = "test@test.com";
        UserResponse expectedResponse = new UserResponse(1L, "", "", "", "", "", "", "test@test.com", "Test", "password", "");

        when(userFeignClient.findByEmail(email)).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        UserResponse actualResponse = userFeignServiceImpl.findByEmail(email);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getEmail(), actualResponse.getEmail());
        verify(userFeignClient, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_ShouldReturnEmptyUserResponse_WhenUserIsNotFound() {
        String email = "test@test.com";

        when(userFeignClient.findByEmail(email)).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        UserResponse actualResponse = userFeignServiceImpl.findByEmail(email);

        assertNull(actualResponse.getId());
        verify(userFeignClient, times(1)).findByEmail(email);
    }
}