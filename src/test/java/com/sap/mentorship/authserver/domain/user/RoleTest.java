package com.sap.mentorship.authserver.domain.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleTest {

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Role("", "description"),
            "Role name must not be empty");
    }

    @Test
    void shouldCreateRole() {
        Role role = new Role("ROLE_USER", "User role");
        Assertions.assertNotNull(role);
    }

}