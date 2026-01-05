package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import org.springframework.stereotype.Repository;

import com.accomputers.api.application.ports.output.repositories.BrandRepositoryInterface;
import com.accomputers.api.domain.entities.Brand;
import com.accomputers.api.infrastructure.persistence.jpa.entities.BrandEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.BrandJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.BrandMapper;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BrandRepository implements BrandRepositoryInterface {
    private final BrandJpaRepository jpaRepository;
    private final BrandMapper mapper;

    public BrandRepository(BrandJpaRepository jpaRepository, BrandMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Brand> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Brand findById(Integer id) {
        if (id == null) {
            return null;
        }
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public Brand findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return jpaRepository.findByName(name)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public Brand save(Brand brand) {
        if (brand == null) {
            return null;
        }

        BrandEntity entity = mapper.toEntity(brand);
        BrandEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return jpaRepository.existsByName(name);
    }
}

