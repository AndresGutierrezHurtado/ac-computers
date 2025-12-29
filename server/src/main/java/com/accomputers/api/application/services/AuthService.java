package com.accomputers.api.application.services;

import java.util.List;

// Domain
import com.accomputers.api.domain.entities.User;
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.exceptions.InvalidValueObjectException;
import com.accomputers.api.domain.valueobjects.Email;
import com.accomputers.api.domain.valueobjects.Password;

// Ports
import com.accomputers.api.application.ports.input.AuthUseCase;
import com.accomputers.api.application.ports.output.PasswordHasher;
import com.accomputers.api.application.ports.output.repositories.UserRepositoryInterface;

// DTOs
import com.accomputers.api.application.dtos.auth.LoginDTO;

public class AuthService implements AuthUseCase {
    private final UserRepositoryInterface userRepository;
    private final PasswordHasher passwordHasher;

    public AuthService(UserRepositoryInterface userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(new Email(loginDTO.email()));

        if (user == null) {
            throw new EntityNotFoundException("User", loginDTO.email());
        }

        if (!passwordHasher.verifyPassword(loginDTO.password(), user.getPassword().getValue())) {
            throw new InvalidValueObjectException("Password", loginDTO.password(), "is incorrect");
        }

        // todo: authenticate user

        return user;
    }

    public User registerUser(String firstName, String lastName, String email, String password, Integer roleId) {
        String hashedPassword = passwordHasher.hashPassword(password);
        User user = new User(null, firstName, lastName, new Email(email), new Password(hashedPassword), roleId);
        return userRepository.save(user);
    }

    public User getUserById(Integer id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Integer id, String firstName, String lastName, String email, Integer roleId) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(new Email(email));
        user.setRoleId(roleId);
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }
        userRepository.delete(id);
    }
}
