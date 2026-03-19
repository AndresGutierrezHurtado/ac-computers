package com.accomputers.api.application.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SalesChatRequest(
        @NotEmpty @Valid List<ChatMessageDto> messages) {
}
