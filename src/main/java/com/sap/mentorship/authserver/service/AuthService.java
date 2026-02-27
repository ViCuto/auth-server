package com.sap.mentorship.authserver.service;

import com.sap.mentorship.authserver.domain.User;
import com.sap.mentorship.authserver.domain.UserCredentials;
import com.sap.mentorship.authserver.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    static final String MSG_USER_REGISTERED = "User registered successfully";
    static final String MSG_USERNAME_EXISTS = "Username already exists";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(MSG_USERNAME_EXISTS);
        }

        User user = new User(username);
        UserCredentials credentials = new UserCredentials(passwordEncoder.encode(password));
        user.setCredentials(credentials);

        userRepository.save(user);
        return MSG_USER_REGISTERED;
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
            .filter(user -> passwordEncoder.matches(password, user.getCredentials().getPassword()));
    }
}
