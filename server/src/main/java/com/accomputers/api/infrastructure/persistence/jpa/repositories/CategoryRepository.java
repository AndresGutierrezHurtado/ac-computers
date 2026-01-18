package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import org.springframework.stereotype.Repository;

import com.accomputers.api.application.ports.output.repositories.CategoryRepositoryInterface;
import com.accomputers.api.domain.entities.Category;
import com.accomputers.api.infrastructure.persistence.jpa.entities.CategoryEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.CategoryJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CategoryRepository implements CategoryRepositoryInterface {
    private final CategoryJpaRepository jpaRepository;
    private final CategoryMapper mapper;

    public CategoryRepository(CategoryJpaRepository jpaRepository, CategoryMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Category findById(Integer id) {
        if (id == null) {
            return null;
        }
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public Category findBySlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            return null;
        }
        return jpaRepository.findBySlug(slug)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public Category findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return jpaRepository.findByName(name)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public Category save(Category category) {
        if (category == null) {
            return null;
        }

        CategoryEntity entity = mapper.toEntity(category);
        CategoryEntity savedEntity = jpaRepository.save(entity);
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
