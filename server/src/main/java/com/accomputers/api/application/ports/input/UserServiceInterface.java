package com.accomputers.api.application.ports.input;

import java.util.List;

// DTOs
import com.accomputers.api.application.dtos.auth.UpdateUserDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;

public interface UserServiceInterface {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Integer id);
    UserResponseDTO updateUser(Integer id, UpdateUserDTO updateUserDTO);
    void deleteUser(Integer id);
}
