package com.accomputers.api.application.services;

import jakarta.transaction.Transactional;

import com.accomputers.api.domain.entities.Role;
// Domain
import com.accomputers.api.domain.entities.User;
import com.accomputers.api.domain.exceptions.EmailAlreadyExistsException;
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.exceptions.InvalidValueObjectException;
import com.accomputers.api.domain.valueobjects.Email;
import com.accomputers.api.domain.valueobjects.Password;

// Ports
import com.accomputers.api.application.ports.input.AuthServiceInterface;
import com.accomputers.api.application.ports.output.LoggerPort;
import com.accomputers.api.application.ports.output.PasswordHasherInterface;
import com.accomputers.api.application.ports.output.UserAuthServiceInterface;
import com.accomputers.api.application.ports.output.repositories.RoleRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.UserRepositoryInterface;

// DTOs
import com.accomputers.api.application.dtos.auth.LoginDTO;
import com.accomputers.api.application.dtos.auth.RegisterDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthServiceInterface {
    private final UserRepositoryInterface userRepository;
    private final PasswordHasherInterface passwordHasher;
    private final RoleRepositoryInterface roleRepository;
    private final UserAuthServiceInterface userAuthService;
    private final LoggerPort loggerPort;

    @Autowired
    public AuthService(UserRepositoryInterface userRepository, PasswordHasherInterface passwordHasher,
            RoleRepositoryInterface roleRepository, UserAuthServiceInterface userAuthService, LoggerPort loggerPort) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.roleRepository = roleRepository;
        this.userAuthService = userAuthService;
        this.loggerPort = loggerPort;
    }

    @Override
    public UserResponseDTO login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(new Email(loginDTO.email()));

        if (user == null) {
            throw new EntityNotFoundException("User", loginDTO.email());
        }

        if (!passwordHasher.verifyPassword(new Password(loginDTO.password()), user.getPassword())) {
            throw new InvalidValueObjectException("Password", loginDTO.password(), "is incorrect");
        }

        userAuthService.authenticateUser(user);

        loggerPort.info(String.format("User logged in successfully - ID: %d, Email: %s",
                user.getId(), user.getEmail().getValue()));

        return UserResponseDTO.fromUser(user);
    }

    @Override
    @Transactional
    public UserResponseDTO register(RegisterDTO registerDTO) {
        User existingUser = userRepository.findByEmail(new Email(registerDTO.email()));

        if (existingUser != null) {
            throw EmailAlreadyExistsException.forEmail(registerDTO.email());
        }

        Password hashedPassword = passwordHasher.hashPassword(new Password(registerDTO.password()));

        Role role = roleRepository.findById(registerDTO.roleId());
        if (role == null) {
            throw new InvalidValueObjectException("Role", registerDTO.roleId(), "does not exist");
        }

        User newUser = new User(
                null,
                registerDTO.firstName(),
                registerDTO.lastName(),
                new Email(registerDTO.email()),
                hashedPassword,
                role.getId());

        User savedUser = userRepository.save(newUser);

        loggerPort.info(String.format("User registered successfully - ID: %d, Email: %s, Name: %s %s",
                savedUser.getId(), savedUser.getEmail().getValue(),
                savedUser.getFirstName(), savedUser.getLastName()));

        return UserResponseDTO.fromUser(savedUser);
    }

    public UserResponseDTO getSession() {
        User user = userAuthService.getAuthenticatedUser();

        if (user == null) {
            throw new EntityNotFoundException("User", "authenticated user not found");
        }

        return UserResponseDTO.fromUser(user);
    }

    @Transactional
    public void logout() {
        User user = userAuthService.getAuthenticatedUser();

        if (user == null) {
            throw new EntityNotFoundException("User", "authenticated user not found");
        }

        loggerPort.info(String.format("User logged out successfully - ID: %d, Email: %s",
                user.getId(), user.getEmail().getValue()));

        userAuthService.logoutUser(user);
    }
}
