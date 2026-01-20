package com.accomputers.api.application.ports.input;

import org.springframework.validation.annotation.Validated;

// DTOs
import com.accomputers.api.application.dtos.auth.LoginDTO;
import com.accomputers.api.application.dtos.auth.RegisterDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;
import jakarta.validation.Valid;

@Validated
public interface AuthServiceInterface {
    UserResponseDTO login(@Valid LoginDTO loginDTO);
    UserResponseDTO register(@Valid RegisterDTO registerDTO);
    UserResponseDTO getSession();
    void logout();
}

