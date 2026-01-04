package com.accomputers.api.application.ports.input;

// DTOs
import com.accomputers.api.application.dtos.auth.LoginDTO;
import com.accomputers.api.application.dtos.auth.LoginResponseDTO;
import com.accomputers.api.application.dtos.auth.RegisterDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;

public interface AuthServiceInterface {
    LoginResponseDTO login(LoginDTO loginDTO);
    UserResponseDTO register(RegisterDTO registerDTO);
}

