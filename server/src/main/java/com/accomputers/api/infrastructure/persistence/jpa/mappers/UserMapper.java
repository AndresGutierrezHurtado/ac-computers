package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.User;
import com.accomputers.api.domain.valueobjects.Email;
import com.accomputers.api.domain.valueobjects.Password;
import com.accomputers.api.infrastructure.persistence.jpa.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    @Autowired
    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

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
            entity.getRoleId()
        );

        // Mapear relación usando mapper
        if (entity.getRole() != null) {
            user.setRole(roleMapper.toDomain(entity.getRole()));
        }

        return user;
    }

    public List<User> toDomain(List<UserEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
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
        entity.setRoleId(domain.getRoleId());

        return entity;
    }

    public List<UserEntity> toEntity(List<User> domains) {
        if (domains == null || domains.isEmpty()) {
            return List.of();
        }
        return domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

