package com.accomputers.api.application.services;

import jakarta.transaction.Transactional;

import com.accomputers.api.domain.entities.PasswordResetToken;
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
import com.accomputers.api.application.ports.output.MessagingService;
import com.accomputers.api.application.ports.output.PasswordHasherInterface;
import com.accomputers.api.application.ports.output.UserAuthServiceInterface;
import com.accomputers.api.application.ports.output.repositories.PasswordResetTokenRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.RoleRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.UserRepositoryInterface;

// DTOs
import com.accomputers.api.application.dtos.auth.ForgotPasswordDTO;
import com.accomputers.api.application.dtos.auth.LoginDTO;
import com.accomputers.api.application.dtos.auth.RegisterDTO;
import com.accomputers.api.application.dtos.auth.SetPasswordDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService implements AuthServiceInterface {
    private final UserRepositoryInterface userRepository;
    private final PasswordHasherInterface passwordHasher;
    private final RoleRepositoryInterface roleRepository;
    private final UserAuthServiceInterface userAuthService;
    private final PasswordResetTokenRepositoryInterface passwordResetTokenRepository;
    private final MessagingService messagingService;
    private final LoggerPort loggerPort;

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Autowired
    public AuthService(UserRepositoryInterface userRepository, PasswordHasherInterface passwordHasher,
            RoleRepositoryInterface roleRepository, UserAuthServiceInterface userAuthService,
            PasswordResetTokenRepositoryInterface passwordResetTokenRepository,
            MessagingService messagingService, LoggerPort loggerPort) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.roleRepository = roleRepository;
        this.userAuthService = userAuthService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.messagingService = messagingService;
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

    @Override
    @Transactional
    public void setPassword(SetPasswordDTO setPasswordDTO) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(setPasswordDTO.token());
        if (resetToken == null) {
            throw new InvalidValueObjectException("Password reset token", setPasswordDTO.token(), "is invalid");
        }
        if (resetToken.isUsed()) {
            throw new InvalidValueObjectException("Password reset token", setPasswordDTO.token(), "is already used");
        }
        if (resetToken.isExpired(LocalDateTime.now())) {
            throw new InvalidValueObjectException("Password reset token", setPasswordDTO.token(), "has expired");
        }

        User user = userRepository.findById(resetToken.getUserId());
        if (user == null) {
            throw new EntityNotFoundException("User", resetToken.getUserId());
        }

        Password hashedPassword = passwordHasher.hashPassword(new Password(setPasswordDTO.password()));
        user.setPassword(hashedPassword);
        userRepository.save(user);

        resetToken.setUsedAt(LocalDateTime.now());
        passwordResetTokenRepository.save(resetToken);

        loggerPort.info(String.format("User password updated via token - ID: %d, Email: %s",
                user.getId(), user.getEmail().getValue()));
    }

    @Override
    @Transactional
    public void requestPasswordReset(ForgotPasswordDTO forgotPasswordDTO) {
        User user = userRepository.findByEmail(new Email(forgotPasswordDTO.email()));

        if (user == null) {
            return;
        }

        PasswordResetToken resetToken = new PasswordResetToken(
                null,
                user.getId(),
                generateToken(),
                LocalDateTime.now().plusHours(24),
                null);

        PasswordResetToken savedToken = passwordResetTokenRepository.save(resetToken);

        String link = frontendUrl.replaceAll("/$", "") + "/set-password?token=" + savedToken.getToken();
        String displayName = user.getFirstName() + " " + user.getLastName();
        messagingService.sendPasswordReset(
                displayName.trim().isEmpty() ? user.getEmail().getValue() : displayName,
                user.getEmail().getValue(),
                link);

        loggerPort.info(String.format("Password reset requested - User ID: %d, Email: %s",
                user.getId(), user.getEmail().getValue()));
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
    }
}
