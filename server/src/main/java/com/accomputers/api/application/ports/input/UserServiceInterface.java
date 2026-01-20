package com.accomputers.api.application.ports.input;

import org.springframework.validation.annotation.Validated;

// DTOs
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.UserFiltersDTO;
import com.accomputers.api.application.dtos.auth.UpdateUserDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;
import jakarta.validation.Valid;

@Validated
public interface UserServiceInterface {
    PageDTO<UserResponseDTO> getAllUsers(UserFiltersDTO queryParams);
    UserResponseDTO getUserById(Integer id);
    UserResponseDTO updateUser(Integer id, @Valid UpdateUserDTO updateUserDTO);
    void deleteUser(Integer id);
}
