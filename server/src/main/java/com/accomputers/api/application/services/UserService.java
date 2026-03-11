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
import com.accomputers.api.application.ports.input.UserServiceInterface;
import com.accomputers.api.application.ports.output.LoggerPort;
import com.accomputers.api.application.ports.output.MessagingService;
import com.accomputers.api.application.ports.output.PasswordHasherInterface;
import com.accomputers.api.application.ports.output.repositories.PasswordResetTokenRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.RoleRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.UserRepositoryInterface;

// DTOs
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.UserCriteria;
import com.accomputers.api.application.dtos.UserFiltersDTO;
import com.accomputers.api.application.dtos.auth.InviteUserDTO;
import com.accomputers.api.application.dtos.auth.UpdateUserDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {
    private final UserRepositoryInterface userRepository;
    private final RoleRepositoryInterface roleRepository;
    private final PasswordHasherInterface passwordHasher;
    private final MessagingService messagingService;
    private final PasswordResetTokenRepositoryInterface passwordResetTokenRepository;
    private final LoggerPort loggerPort;

    @Autowired
    public UserService(
            UserRepositoryInterface userRepository,
            RoleRepositoryInterface roleRepository,
            PasswordHasherInterface passwordHasher,
            MessagingService messagingService,
            PasswordResetTokenRepositoryInterface passwordResetTokenRepository,
            LoggerPort loggerPort) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordHasher = passwordHasher;
        this.messagingService = messagingService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.loggerPort = loggerPort;
    }

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public PageDTO<UserResponseDTO> getAllUsers(UserFiltersDTO queryParams) {
        UserCriteria userCriteria = queryParams.toUserCriteria();
        PageDTO<User> pageDTO = userRepository.findAll(userCriteria);
        List<UserResponseDTO> userResponseDTOs = pageDTO.data().stream()
                .map(UserResponseDTO::fromUser)
                .collect(Collectors.toList());
        return new PageDTO<>(userResponseDTOs, pageDTO.total());
    }

    @Override
    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }

        return UserResponseDTO.fromUser(user);
    }

    @Override
    @Transactional
    public UserResponseDTO inviteUser(InviteUserDTO inviteUserDTO) {
        User existingUser = userRepository.findByEmail(new Email(inviteUserDTO.email()));
        if (existingUser != null) {
            throw EmailAlreadyExistsException.forEmail(inviteUserDTO.email());
        }

        Role role = roleRepository.findById(inviteUserDTO.roleId());
        if (role == null) {
            throw new InvalidValueObjectException("Role", inviteUserDTO.roleId(), "does not exist");
        }

        String tempPassword = generateTempPassword();
        Password hashedPassword = passwordHasher.hashPassword(new Password(tempPassword));

        User newUser = new User(
                null,
                inviteUserDTO.firstName(),
                inviteUserDTO.lastName(),
                new Email(inviteUserDTO.email()),
                hashedPassword,
                role.getId());

        User savedUser = userRepository.save(newUser);

        PasswordResetToken resetToken = new PasswordResetToken(
                null,
                savedUser.getId(),
                generateToken(),
                LocalDateTime.now().plusHours(24),
                null);

        PasswordResetToken savedToken = passwordResetTokenRepository.save(resetToken);

        String link = frontendUrl.replaceAll("/$", "") + "/set-password?token=" + savedToken.getToken();
        messagingService.sendPasswordSetup(
                savedUser.getFirstName() + " " + savedUser.getLastName(),
                savedUser.getEmail().getValue(),
                link);

        loggerPort.info(String.format("User invited successfully - ID: %d, Email: %s",
                savedUser.getId(), savedUser.getEmail().getValue()));

        return UserResponseDTO.fromUser(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Integer id, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }

        // Check if email is being changed and if it already exists
        if (updateUserDTO.email() != null && !user.getEmail().getValue().equals(updateUserDTO.email())) {
            User existingUser = userRepository.findByEmail(new Email(updateUserDTO.email()));
            if (existingUser != null && !existingUser.getId().equals(id)) {
                throw EmailAlreadyExistsException.forEmail(updateUserDTO.email());
            }
            user.setEmail(new Email(updateUserDTO.email()));
        }

        // Update other fields if provided
        if (updateUserDTO.firstName() != null) {
            user.setFirstName(updateUserDTO.firstName());
        }
        if (updateUserDTO.lastName() != null) {
            user.setLastName(updateUserDTO.lastName());
        }
        if (updateUserDTO.roleId() != null) {
            Role role = roleRepository.findById(updateUserDTO.roleId());
            if (role == null) {
                throw new InvalidValueObjectException("Role", updateUserDTO.roleId(), "does not exist");
            }
            user.setRoleId(role.getId());
        }

        User updatedUser = userRepository.save(user);
        
        loggerPort.info(String.format("User updated successfully - ID: %d, Email: %s, Name: %s %s", 
            updatedUser.getId(), updatedUser.getEmail().getValue(), 
            updatedUser.getFirstName(), updatedUser.getLastName()));
        
        return UserResponseDTO.fromUser(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }

        loggerPort.info(String.format("User deleted successfully - ID: %d, Email: %s, Name: %s %s", 
            user.getId(), user.getEmail().getValue(), user.getFirstName(), user.getLastName()));

        userRepository.delete(id);
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
    }

    private String generateTempPassword() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@$?";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
