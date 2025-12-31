package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import org.springframework.stereotype.Repository;

import com.accomputers.api.application.ports.output.repositories.RoleRepositoryInterface;
import com.accomputers.api.domain.entities.Role;
import com.accomputers.api.infrastructure.persistence.jpa.entities.RoleEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.RoleJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.RoleMapper;

@Repository
public class RoleRepository implements RoleRepositoryInterface {
    private final RoleJpaRepository jpaRepository;

    public RoleRepository(RoleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public Role save(Role role) {
        if (role == null) {
            return null;
        }

        RoleEntity entity = RoleMapper.toEntity(role);
        RoleEntity savedEntity = this.jpaRepository.save(entity);

        return RoleMapper.toDomain(savedEntity);
    }
}
