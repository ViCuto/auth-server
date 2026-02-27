package com.sap.mentorship.authserver.controller;

import com.sap.mentorship.authserver.domain.User;
import com.sap.mentorship.authserver.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testRegisterSuccess() {
        AuthRequest request = new AuthRequest("newuser", "password");
        when(authService.register("newuser", "password")).thenReturn("User registered successfully");

        ResponseEntity<?> response = authController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP 200 OK on successful registration");
        assertEquals("User registered successfully", response.getBody(), "Expected success message in response");
    }

    @Test
    void testRegisterUserExistsReturnsBadRequest() {
        AuthRequest request = new AuthRequest("existinguser", "password");
        when(authService.register("existinguser", "password")).thenThrow(new IllegalArgumentException("Username already exists"));

        ResponseEntity<?> response = authController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected HTTP 400 Bad Request when user exists");
        assertEquals("Username already exists", response.getBody(), "Expected error message in response body");
    }

    @Test
    void testLoginSuccess() {
        AuthRequest request = new AuthRequest("testuser", "password");
        User user = new User("testuser");
        when(authService.login("testuser", "password")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP 200 OK on successful login");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertInstanceOf(LoginResponse.class, response.getBody(), "Expected LoginResponse body");
        LoginResponse body = (LoginResponse) response.getBody();
        assertEquals(AuthController.MSG_LOGIN_SUCCESSFUL, body.message(), "Expected success message in response");
        assertEquals("testuser", body.username(), "Expected username in response");
    }

    @Test
    void testLoginInvalidCredentialsReturnsUnauthorized() {
        AuthRequest request = new AuthRequest("testuser", "wrongpassword");
        when(authService.login("testuser", "wrongpassword")).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Expected HTTP 401 Unauthorized for invalid credentials");
        assertEquals(AuthController.MSG_INVALID_CREDENTIALS, response.getBody(), "Expected invalid credentials message");
    }

}
