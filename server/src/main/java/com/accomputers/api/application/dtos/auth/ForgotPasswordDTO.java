package com.accomputers.api.application.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ForgotPasswordDTO(
        @NotNull @NotBlank(message = "Email is required") @Email(message = "Invalid email address") String email) {
}
