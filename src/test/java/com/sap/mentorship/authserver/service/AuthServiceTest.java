package com.sap.mentorship.authserver.service;

import com.sap.mentorship.authserver.domain.User;
import com.sap.mentorship.authserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private String username;
    private String password;
    private String encodedPassword;

    @BeforeEach
    void setUp() {
        username = "testuser";
        password = "password";
        encodedPassword = "encodedPassword";
    }

    @Test
    void testRegisterSuccess() {
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        String result = authService.register(username, password);

        assertEquals(AuthService.MSG_USER_REGISTERED, result, "Expected success message on registration");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUserExistsThrowsException() {
        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
            () -> authService.register(username, password),
            "Expected IllegalArgumentException when user already exists");
    }

    @Test
    void testLoginSuccess() {
        User user = new User(username);
        user.setCredentials(new com.sap.mentorship.authserver.domain.UserCredentials(encodedPassword));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        Optional<User> result = authService.login(username, password);

        assertTrue(result.isPresent(), "Expected user to be found on successful login");
        assertEquals(username, result.get().getUsername(), "Expected username to match");
    }

    @Test
    void testLoginInvalidPasswordReturnsEmpty() {
        User user = new User(username);
        user.setCredentials(new com.sap.mentorship.authserver.domain.UserCredentials(encodedPassword));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        Optional<User> result = authService.login(username, password);

        assertFalse(result.isPresent(), "Expected login to fail with invalid password");
    }

    @Test
    void testLoginUserNotFoundReturnsEmpty() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = authService.login(username, password);

        assertFalse(result.isPresent(), "Expected login to fail when user is not found");
    }

    @Test
    void testLoginNullCredentialsReturnsEmpty() {
        User user = new User(username);
        user.setCredentials(null);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = authService.login(username, password);

        assertFalse(result.isPresent(), "Expected login to fail when user has no credentials");
    }

    @Test
    void testRegisterNullUsernameThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> authService.register(null, password),
            "Expected IllegalArgumentException for null username");
    }

    @Test
    void testRegisterBlankPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> authService.register(username, "   "),
            "Expected IllegalArgumentException for blank password");
    }

}
