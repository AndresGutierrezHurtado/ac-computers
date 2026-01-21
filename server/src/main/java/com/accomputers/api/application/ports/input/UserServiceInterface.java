package com.accomputers.api.application.ports.input;

// DTOs
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.UserFiltersDTO;
import com.accomputers.api.application.dtos.auth.UpdateUserDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;

public interface UserServiceInterface {
    PageDTO<UserResponseDTO> getAllUsers(UserFiltersDTO queryParams);
    UserResponseDTO getUserById(Integer id);
    UserResponseDTO updateUser(Integer id, UpdateUserDTO updateUserDTO);
    void deleteUser(Integer id);
}
