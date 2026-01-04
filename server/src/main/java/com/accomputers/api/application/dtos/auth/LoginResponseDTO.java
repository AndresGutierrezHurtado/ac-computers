package com.accomputers.api.application.dtos.auth;

import com.accomputers.api.application.dtos.response.UserResponseDTO;

public record LoginResponseDTO(
        UserResponseDTO user,
        String token
) {
}

