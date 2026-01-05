package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import org.springframework.stereotype.Repository;

import com.accomputers.api.application.ports.output.repositories.SpecificationValueRepositoryInterface;
import com.accomputers.api.domain.entities.SpecificationValue;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SpecificationEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SpecificationValueEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.SpecificationJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.SpecificationValueJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.SpecificationValueMapper;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SpecificationValueRepository implements SpecificationValueRepositoryInterface {
    private final SpecificationValueJpaRepository jpaRepository;
    private final SpecificationJpaRepository specificationJpaRepository;
    private final SpecificationValueMapper mapper;

    public SpecificationValueRepository(
            SpecificationValueJpaRepository jpaRepository,
            SpecificationJpaRepository specificationJpaRepository,
            SpecificationValueMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.specificationJpaRepository = specificationJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<SpecificationValue> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public SpecificationValue findById(Integer id) {
        if (id == null) {
            return null;
        }
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<SpecificationValue> findBySpecificationId(Integer specificationId) {
        if (specificationId == null) {
            return List.of();
        }
        return jpaRepository.findBySpecificationId(specificationId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SpecificationValue> findBySpecificationIdOrderByOrderAsc(Integer specificationId) {
        if (specificationId == null) {
            return List.of();
        }
        return jpaRepository.findBySpecificationIdOrderByOrderAsc(specificationId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public SpecificationValue findBySpecificationIdAndValue(Integer specificationId, String value) {
        if (specificationId == null || value == null || value.trim().isEmpty()) {
            return null;
        }
        return jpaRepository.findBySpecificationIdAndValue(specificationId, value)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public SpecificationValue save(SpecificationValue specificationValue) {
        if (specificationValue == null) {
            return null;
        }

        SpecificationValueEntity entity = mapper.toEntity(specificationValue);
        
        // Set specification relationship if specificationId is provided
        if (specificationValue.getSpecificationId() != null) {
            SpecificationEntity specificationEntity = specificationJpaRepository.findById(specificationValue.getSpecificationId())
                    .orElse(null);
            if (specificationEntity != null) {
                entity.setSpecification(specificationEntity);
            }
        }

        SpecificationValueEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}

