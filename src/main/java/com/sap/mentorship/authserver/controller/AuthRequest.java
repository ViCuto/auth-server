package com.sap.mentorship.authserver.controller;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    @NotBlank(message = MSG_USERNAME_REQUIRED)
    String username,

    @NotBlank(message = MSG_PASSWORD_REQUIRED)
    String password
) {
    private static final String MSG_USERNAME_REQUIRED = "Username is required";
    private static final String MSG_PASSWORD_REQUIRED = "Password is required";

}
