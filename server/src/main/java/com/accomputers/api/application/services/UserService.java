package com.accomputers.api.application.services;

// Domain
import com.accomputers.api.domain.entities.User;
import com.accomputers.api.domain.exceptions.EmailAlreadyExistsException;
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.valueobjects.Email;

// Ports
import com.accomputers.api.application.ports.input.UserServiceInterface;
import com.accomputers.api.application.ports.output.repositories.UserRepositoryInterface;

// DTOs
import com.accomputers.api.application.dtos.auth.UpdateUserDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {
    private final UserRepositoryInterface userRepository;

    @Autowired
    public UserService(UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponseDTO::fromUser)
                .collect(Collectors.toList());
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
            user.setRoleId(updateUserDTO.roleId());
        }

        User updatedUser = userRepository.save(user);
        return UserResponseDTO.fromUser(updatedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }

        userRepository.delete(id);
    }
}
