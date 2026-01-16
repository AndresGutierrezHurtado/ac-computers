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
    private final RoleMapper mapper;

    public RoleRepository(RoleJpaRepository jpaRepository, RoleMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Role findById(Integer id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public Role save(Role role) {
        if (role == null) {
            return null;
        }

        RoleEntity entity = mapper.toEntity(role);
        RoleEntity savedEntity = this.jpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }
}
