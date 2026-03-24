package com.accomputers.api.application.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GoogleLoginDTO(
        @NotNull
        @NotBlank(message = "Google credential is required")
        String credential) {
}

