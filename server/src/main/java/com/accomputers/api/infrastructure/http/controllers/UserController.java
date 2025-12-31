package com.accomputers.api.infrastructure.http.controllers;

// Spring
import com.accomputers.api.application.dtos.response.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Application
import com.accomputers.api.application.dtos.auth.UpdateUserDTO;
import com.accomputers.api.application.ports.input.UserServiceInterface;

// Infrastructure
import com.accomputers.api.infrastructure.http.ResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceInterface userServiceInterface;

    @Autowired
    public UserController(UserServiceInterface userServiceInterface) {
        this.userServiceInterface = userServiceInterface;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userServiceInterface.getAllUsers();

        ResponseDTO<List<UserResponseDTO>> responseDTO = new ResponseDTO<>(
                "Users retrieved successfully",
                true,
                users);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> getUserById(@PathVariable Integer id) {
        UserResponseDTO user = userServiceInterface.getUserById(id);

        ResponseDTO<UserResponseDTO> responseDTO = new ResponseDTO<>(
                "User retrieved successfully",
                true,
                user);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> updateUser(
            @PathVariable Integer id,
            @RequestBody UpdateUserDTO updateUserDTO) {

        UserResponseDTO user = userServiceInterface.updateUser(id, updateUserDTO);

        ResponseDTO<UserResponseDTO> responseDTO = new ResponseDTO<>(
                "User updated successfully",
                true,
                user);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteUser(@PathVariable Integer id) {
        userServiceInterface.deleteUser(id);

        ResponseDTO<Void> responseDTO = new ResponseDTO<>(
                "User deleted successfully",
                true);

        return ResponseEntity.ok(responseDTO);
    }
}
