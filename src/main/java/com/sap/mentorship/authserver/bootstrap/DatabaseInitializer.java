package com.sap.mentorship.authserver.bootstrap;

import com.sap.mentorship.authserver.domain.AppUser;
import com.sap.mentorship.authserver.domain.Role;
import com.sap.mentorship.authserver.domain.UserCredentials;
import com.sap.mentorship.authserver.repository.RoleRepository;
import com.sap.mentorship.authserver.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserRepository userRepository, RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String @NonNull ... args) throws Exception {
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "Administrator with full rights");
        Role userRole = createRoleIfNotFound("ROLE_USER", "Standard user");

        if (userRepository.findByUsername("admin").isEmpty()) {
            System.out.println("Initializing Database with Admin User");

            AppUser admin = new AppUser("admin", "admin@example.com");
            admin.addRole(adminRole);
            admin.addRole(userRole);

            String encodedPassword = passwordEncoder.encode("password");
            UserCredentials credentials = new UserCredentials(encodedPassword);

            admin.setCredentials(credentials);

            userRepository.save(admin);

            System.out.println("Admin created");
        } else {
            System.out.println("Admin user already exists");
        }
    }

    private Role createRoleIfNotFound(String name, String description) {
        Optional<Role> roleOptional = roleRepository.findByName(name);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        Role role = new Role(name, description);
        return roleRepository.save(role);
    }
}