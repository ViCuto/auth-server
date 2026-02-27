package com.sap.mentorship.authserver.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    private static final String MSG_USERNAME_EMPTY = "Username must not be empty";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    private boolean enabled = true;

    private Instant createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserCredentials credentials;

    protected User() {
    }

    public User(String username) {
        Assert.hasText(username, MSG_USERNAME_EMPTY);
        this.username = username;
        this.createdAt = Instant.now();
    }

    public void setCredentials(UserCredentials credentials) {
        this.credentials = credentials;
        if (credentials != null) {
            credentials.setUser(this);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UserCredentials getCredentials() {
        return credentials;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
