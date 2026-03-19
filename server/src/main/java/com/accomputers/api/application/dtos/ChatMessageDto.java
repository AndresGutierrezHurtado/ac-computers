package com.accomputers.api.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageDto(
        @NotNull ChatRole role,
        @NotBlank String content) {
}
