package com.sap.mentorship.authserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_credentials")
public class UserCredentials {
    private static final String MSG_PASSWORD_EMPTY = "Password must not be empty";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String password;

    private Instant lastPasswordReset;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    protected UserCredentials() {
    }

    public UserCredentials(String password) {
        Assert.hasText(password, MSG_PASSWORD_EMPTY);
        this.password = password;
        this.lastPasswordReset = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Instant getLastPasswordReset() {
        return lastPasswordReset;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPassword(String password) {
        Assert.hasText(password, MSG_PASSWORD_EMPTY);
        this.password = password;
        this.lastPasswordReset = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        UserCredentials that = (UserCredentials) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
