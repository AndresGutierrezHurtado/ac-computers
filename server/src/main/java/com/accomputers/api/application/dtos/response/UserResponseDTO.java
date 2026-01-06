package com.accomputers.api.application.dtos.response;

import com.accomputers.api.domain.entities.Role;
import com.accomputers.api.domain.entities.User;

public record UserResponseDTO(
        Integer id,
        String firstName,
        String lastName,
        String email,
        Role role) {

    public static UserResponseDTO fromUser(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail() != null ? user.getEmail().getValue() : null,
                user.getRole());
    }
}
