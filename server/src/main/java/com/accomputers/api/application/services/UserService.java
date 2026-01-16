package com.accomputers.api.application.services;

import jakarta.transaction.Transactional;

import com.accomputers.api.domain.entities.Role;
// Domain
import com.accomputers.api.domain.entities.User;
import com.accomputers.api.domain.exceptions.EmailAlreadyExistsException;
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.exceptions.InvalidValueObjectException;
import com.accomputers.api.domain.valueobjects.Email;

// Ports
import com.accomputers.api.application.ports.input.UserServiceInterface;
import com.accomputers.api.application.ports.output.LoggerPort;
import com.accomputers.api.application.ports.output.repositories.RoleRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.UserRepositoryInterface;

// DTOs
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.UserCriteria;
import com.accomputers.api.application.dtos.UserFiltersDTO;
import com.accomputers.api.application.dtos.auth.UpdateUserDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {
    private final UserRepositoryInterface userRepository;
    private final RoleRepositoryInterface roleRepository;
    private final LoggerPort loggerPort;

    @Autowired
    public UserService(UserRepositoryInterface userRepository, RoleRepositoryInterface roleRepository, LoggerPort loggerPort) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.loggerPort = loggerPort;
    }

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
}
