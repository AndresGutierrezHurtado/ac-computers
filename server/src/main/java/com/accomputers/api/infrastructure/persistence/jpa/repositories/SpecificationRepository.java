package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import org.springframework.stereotype.Repository;

import com.accomputers.api.application.ports.output.repositories.SpecificationRepositoryInterface;
import com.accomputers.api.domain.entities.Specification;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SubCategoryEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SpecificationEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.SubCategoryJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.SpecificationJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.SpecificationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SpecificationRepository implements SpecificationRepositoryInterface {
    private final SpecificationJpaRepository jpaRepository;
    private final SubCategoryJpaRepository subCategoryJpaRepository;
    private final SpecificationMapper mapper;

    public SpecificationRepository(
            SpecificationJpaRepository jpaRepository,
            SubCategoryJpaRepository subCategoryJpaRepository,
            SpecificationMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.subCategoryJpaRepository = subCategoryJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Specification> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Specification findById(Integer id) {
        if (id == null) {
            return null;
        }
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public Specification findBySlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            return null;
        }
        return jpaRepository.findBySlug(slug)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public Specification findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return jpaRepository.findByName(name)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<Specification> findBySubCategoryId(Integer subCategoryId) {
        if (subCategoryId == null) {
            return List.of();
        }
        return jpaRepository.findBySubCategoryId(subCategoryId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Specification> findBySubCategoryIdAndIsFilterableTrue(Integer subCategoryId) {
        if (subCategoryId == null) {
            return List.of();
        }
        return jpaRepository.findBySubCategoryIdAndIsFilterableTrue(subCategoryId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Specification> findBySubCategoryIdAndIsMandatoryTrue(Integer subCategoryId) {
        if (subCategoryId == null) {
            return List.of();
        }
        return jpaRepository.findBySubCategoryIdAndIsMandatoryTrue(subCategoryId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Specification save(Specification specification) {
        if (specification == null) {
            return null;
        }

        SpecificationEntity entity = mapper.toEntity(specification);
        
        // Set subCategory relationship if subCategoryId is provided
        if (specification.getSubCategoryId() != null) {
            SubCategoryEntity subCategoryEntity = subCategoryJpaRepository.findById(specification.getSubCategoryId())
                    .orElse(null);
            entity.setSubCategory(subCategoryEntity);
        }

        SpecificationEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsBySlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            return false;
        }
        return jpaRepository.existsBySlug(slug);
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return jpaRepository.existsByName(name);
    }
}

