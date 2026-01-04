package com.accomputers.api.application.ports.input;

// DTOs
import com.accomputers.api.application.dtos.auth.LoginDTO;
import com.accomputers.api.application.dtos.auth.RegisterDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;

public interface AuthServiceInterface {
    UserResponseDTO login(LoginDTO loginDTO);
    UserResponseDTO register(RegisterDTO registerDTO);
    UserResponseDTO getSession();
    void logout();
}

