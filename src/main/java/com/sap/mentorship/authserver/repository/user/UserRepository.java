package com.sap.mentorship.authserver.repository.user;

import com.sap.mentorship.authserver.domain.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
