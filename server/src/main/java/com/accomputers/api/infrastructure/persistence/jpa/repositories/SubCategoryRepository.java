package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import org.springframework.stereotype.Repository;

import com.accomputers.api.application.ports.output.repositories.SubCategoryRepositoryInterface;
import com.accomputers.api.domain.entities.SubCategory;
import com.accomputers.api.infrastructure.persistence.jpa.entities.CategoryEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SubCategoryEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.CategoryJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.SubCategoryJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.SubCategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SubCategoryRepository implements SubCategoryRepositoryInterface {
    private final SubCategoryJpaRepository jpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final SubCategoryMapper mapper;

    public SubCategoryRepository(
            SubCategoryJpaRepository jpaRepository,
            CategoryJpaRepository categoryJpaRepository,
            SubCategoryMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<SubCategory> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public SubCategory findById(Integer id) {
        if (id == null) {
            return null;
        }
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public SubCategory findBySlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            return null;
        }
        return jpaRepository.findBySlug(slug)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public SubCategory findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return jpaRepository.findByName(name)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<SubCategory> findByCategoryId(Integer categoryId) {
        if (categoryId == null) {
            return List.of();
        }
        return jpaRepository.findByCategoryId(categoryId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public SubCategory save(SubCategory subCategory) {
        if (subCategory == null) {
            return null;
        }

        SubCategoryEntity entity = mapper.toEntity(subCategory);
        
        // Set category relationship if categoryId is provided
        if (subCategory.getCategoryId() != null) {
            CategoryEntity categoryEntity = categoryJpaRepository.findById(subCategory.getCategoryId())
                    .orElse(null);
            entity.setCategory(categoryEntity);
        }

        SubCategoryEntity savedEntity = jpaRepository.save(entity);
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

