package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import org.springframework.stereotype.Component;

// Domain
import com.accomputers.api.domain.entities.Role;

// Entities
import com.accomputers.api.infrastructure.persistence.jpa.entities.RoleEntity;

@Component
public class RoleMapper {
    
    public static Role toDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }

        Role role = new Role(
            entity.getId(),
            entity.getName()
        );

        return role;
    }

    public static RoleEntity toEntity(Role domain) {
        if (domain == null) {
            return null;
        }

        RoleEntity entity = new RoleEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());

        return entity;
    }
}
