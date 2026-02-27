package com.sap.mentorship.authserver.controller;

import com.sap.mentorship.authserver.domain.User;
import com.sap.mentorship.authserver.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    static final String MSG_LOGIN_SUCCESSFUL = "Login successful";
    static final String MSG_INVALID_CREDENTIALS = "Invalid username or password";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request) {
        try {
            String message = authService.register(request.username(), request.password());
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        Optional<User> userOptional = authService.login(request.username(), request.password());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(new LoginResponse(MSG_LOGIN_SUCCESSFUL, user.getUsername()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(MSG_INVALID_CREDENTIALS);
    }
}
