package com.sap.mentorship.authserver.bootstrap;

import com.sap.mentorship.authserver.domain.User;
import com.sap.mentorship.authserver.domain.UserCredentials;
import com.sap.mentorship.authserver.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final String MSG_INITIALIZING_ADMIN = "Initializing Database with Admin User";
    private static final String MSG_ADMIN_CREATED = "Admin created";
    private static final String MSG_ADMIN_EXISTS = "Admin user already exists";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.default-username}")
    private String defaultUsername;

    @Value("${admin.default-password}")
    private String defaultPassword;

    public DatabaseInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String @NonNull ... args) {
        if (userRepository.findByUsername(defaultUsername).isEmpty()) {
            System.out.println(MSG_INITIALIZING_ADMIN);

            User admin = new User(defaultUsername);

            String encodedPassword = passwordEncoder.encode(defaultPassword);
            UserCredentials credentials = new UserCredentials(encodedPassword);

            admin.setCredentials(credentials);

            userRepository.save(admin);

            System.out.println(MSG_ADMIN_CREATED);
        } else {
            System.out.println(MSG_ADMIN_EXISTS);
        }
    }

}