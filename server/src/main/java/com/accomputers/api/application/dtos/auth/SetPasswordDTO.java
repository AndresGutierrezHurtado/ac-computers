package com.accomputers.api.application.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SetPasswordDTO(
        @NotNull
        @NotBlank(message = "Token is required")
        String token,

        @NotNull
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password) {
}
