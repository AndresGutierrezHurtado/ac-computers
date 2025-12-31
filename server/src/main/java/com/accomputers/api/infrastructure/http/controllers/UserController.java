package com.accomputers.api.infrastructure.http.controllers;

// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Application
import com.accomputers.api.application.dtos.auth.RegisterDTO;
import com.accomputers.api.application.dtos.auth.UpdateUserDTO;
import com.accomputers.api.application.ports.input.AuthUseCase;

// Domain
import com.accomputers.api.domain.entities.User;

// Infrastructure
import com.accomputers.api.infrastructure.http.ResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final AuthUseCase authUseCase;

    @Autowired
    public UserController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<User>>> getAllUsers() {
        List<User> users = authUseCase.getAllUsers();

        ResponseDTO<List<User>> responseDTO = new ResponseDTO<>(
            "Users retrieved successfully",
            true,
            users
        );

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<User>> getUserById(@PathVariable Integer id) {
        User user = authUseCase.getUserById(id);

        ResponseDTO<User> responseDTO = new ResponseDTO<>(
            "User retrieved successfully",
            true,
            user
        );

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<User>> createUser(@RequestBody RegisterDTO registerDTO) {
        User user = authUseCase.registerUser(
            registerDTO.firstName(),
            registerDTO.lastName(),
            registerDTO.email(),
            registerDTO.password(),
            registerDTO.roleId()
        );

        ResponseDTO<User> responseDTO = new ResponseDTO<>(
            "User created successfully",
            true,
            user
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<User>> updateUser(
        @PathVariable Integer id,
        @RequestBody UpdateUserDTO updateUserDTO
    ) {
        User user = authUseCase.updateUser(
            id,
            updateUserDTO.firstName(),
            updateUserDTO.lastName(),
            updateUserDTO.email(),
            updateUserDTO.roleId()
        );

        ResponseDTO<User> responseDTO = new ResponseDTO<>(
            "User updated successfully",
            true,
            user
        );

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteUser(@PathVariable Integer id) {
        authUseCase.deleteUser(id);

        ResponseDTO<Void> responseDTO = new ResponseDTO<>(
            "User deleted successfully",
            true
        );

        return ResponseEntity.ok(responseDTO);
    }
}

