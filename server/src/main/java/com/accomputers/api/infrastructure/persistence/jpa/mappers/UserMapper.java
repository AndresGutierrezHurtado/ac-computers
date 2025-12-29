package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.Role;
import com.accomputers.api.domain.entities.User;
import com.accomputers.api.domain.valueobjects.Email;
import com.accomputers.api.domain.valueobjects.Password;
import com.accomputers.api.infrastructure.persistence.jpa.entities.RoleEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User(
            entity.getId(),
            entity.getFirstName(),
            entity.getLastName(),
            new Email(entity.getEmail()),
            new Password(entity.getPassword()),
            entity.getRole() != null ? entity.getRole().getId() : null
        );

        if (entity.getRole() != null) {
            Role role = new Role(
                entity.getRole().getId(),
                entity.getRole().getName()
            );
            user.setRole(role);
        }

        return user;
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setEmail(domain.getEmail() != null ? domain.getEmail().getValue() : null);
        entity.setPassword(domain.getPassword() != null ? domain.getPassword().getValue() : null);

        if (domain.getRoleId() != null) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setId(domain.getRoleId());
            if (domain.getRole() != null) {
                roleEntity.setName(domain.getRole().getName());
            }
            entity.setRole(roleEntity);
        }

        return entity;
    }
}

