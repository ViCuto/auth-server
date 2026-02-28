package com.sap.mentorship.authserver.bootstrap;

import com.sap.mentorship.authserver.domain.User;
import com.sap.mentorship.authserver.domain.UserCredentials;
import com.sap.mentorship.authserver.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    private static final String MSG_INITIALIZING_ADMIN = "Initializing Database with Admin User";
    private static final String MSG_ADMIN_CREATED = "Admin created";
    private static final String MSG_ADMIN_EXISTS = "Admin user already exists";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    public DatabaseInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String @NonNull ... args) {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            log.info(MSG_INITIALIZING_ADMIN + ": {}", adminUsername);

            User admin = new User(adminUsername);

            String encodedPassword = passwordEncoder.encode(adminPassword);
            UserCredentials credentials = new UserCredentials(encodedPassword);

            admin.setCredentials(credentials);

            userRepository.save(admin);

            log.info(MSG_ADMIN_CREATED);
        } else {
            log.info(MSG_ADMIN_EXISTS);
        }
    }

}