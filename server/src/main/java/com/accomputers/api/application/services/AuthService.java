package com.accomputers.api.application.services;

// Domain
import com.accomputers.api.domain.entities.User;
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.exceptions.InvalidValueObjectException;
import com.accomputers.api.domain.valueobjects.Email;
import com.accomputers.api.domain.valueobjects.Password;

// Ports
import com.accomputers.api.application.ports.input.AuthServiceInterface;
import com.accomputers.api.application.ports.output.PasswordHasherInterface;
import com.accomputers.api.application.ports.output.UserAuthServiceInterface;
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
    private final UserAuthServiceInterface userAuthService;

    @Autowired
    public AuthService(UserRepositoryInterface userRepository, PasswordHasherInterface passwordHasher, UserAuthServiceInterface userAuthService) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.userAuthService = userAuthService;
    }

    public UserResponseDTO login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(new Email(loginDTO.email()));

        if (user == null) {
            throw new EntityNotFoundException("User", loginDTO.email());
        }

        if (!passwordHasher.verifyPassword(loginDTO.password(), user.getPassword().getValue())) {
            throw new InvalidValueObjectException("Password", loginDTO.password(), "is incorrect");
        }

        userAuthService.authenticateUser(user);

        return UserResponseDTO.fromUser(user);
    }

    public UserResponseDTO register(RegisterDTO registerDTO) {
        String hashedPassword = passwordHasher.hashPassword(registerDTO.password());

        User savedUser = userRepository
                .save(
                        new User(
                                null,
                                registerDTO.firstName(),
                                registerDTO.lastName(),
                                new Email(registerDTO.email()),
                                new Password(hashedPassword),
                                registerDTO.roleId()));

        return UserResponseDTO.fromUser(savedUser);
    }

    public UserResponseDTO getSession() {
        User user = userAuthService.getAuthenticatedUser();

        if (user == null) {
            throw new EntityNotFoundException("User", "authenticated user not found");
        }

        return UserResponseDTO.fromUser(user);
    }

    public void logout() {
        User user = userAuthService.getAuthenticatedUser();

        if (user == null) {
            throw new EntityNotFoundException("User", "authenticated user not found");
        }

        userAuthService.logoutUser(user);
    }
}
