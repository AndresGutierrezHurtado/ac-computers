package com.accomputers.api.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ContactDTO(
    @NotNull
    @NotBlank(message = "Subject is required")
    @Size(min = 3, max = 100, message = "Subject must be between 3 and 100 characters")
    String subject,

    @NotNull
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    String name,

    @NotNull
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    String email,

    @NotNull
    @NotBlank(message = "Message is required")
    @Size(min = 3, max = 1000, message = "Message must be between 3 and 1000 characters")
    String message
) {
}
