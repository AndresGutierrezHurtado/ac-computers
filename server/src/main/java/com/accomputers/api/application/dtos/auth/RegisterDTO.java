package com.accomputers.api.application.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

public record RegisterDTO(
        @NotNull
        @NotBlank(message = "First name is required")
        @Size(min = 3, max = 100, message = "First name must be between 3 and 100 characters")
        String firstName,
        
        @NotNull
        @NotBlank(message = "Last name is required")
        @Size(min = 3, max = 100, message = "Last name must be between 3 and 100 characters")
        String lastName,
        
        @NotNull
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email address")
        String email,
        
        @NotNull
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password,
        
        @NotNull
        @Positive(message = "Role ID must be positive")
        Integer roleId) {
}

