package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.Category;
import com.accomputers.api.domain.valueobjects.Slug;
import com.accomputers.api.infrastructure.persistence.jpa.entities.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public Category toDomain(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        Category category = new Category(
                entity.getId(),
                entity.getName(),
                new Slug(entity.getSlug()),
                entity.getDescription());

        return category;
    }

    public List<Category> toDomain(List<CategoryEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public CategoryEntity toEntity(Category domain) {
        if (domain == null) {
            return null;
        }

        CategoryEntity entity = new CategoryEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setSlug(domain.getSlug() != null ? domain.getSlug().getValue() : null);
        entity.setDescription(domain.getDescription());

        return entity;
    }

    public List<CategoryEntity> toEntity(List<Category> domains) {
        if (domains == null || domains.isEmpty()) {
            return List.of();
        }
        return domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
