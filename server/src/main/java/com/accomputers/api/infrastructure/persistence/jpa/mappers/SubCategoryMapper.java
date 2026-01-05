package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.SubCategory;
import com.accomputers.api.domain.valueobjects.Slug;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SubCategoryEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubCategoryMapper {

    public SubCategory toDomain(SubCategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        SubCategory subCategory = new SubCategory(
                entity.getId(),
                entity.getName(),
                new Slug(entity.getSlug()),
                entity.getDescription(),
                entity.getCategory() != null ? entity.getCategory().getId() : null);

        return subCategory;
    }

    public List<SubCategory> toDomain(List<SubCategoryEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public SubCategoryEntity toEntity(SubCategory domain) {
        if (domain == null) {
            return null;
        }

        SubCategoryEntity entity = new SubCategoryEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setSlug(domain.getSlug() != null ? domain.getSlug().getValue() : null);
        entity.setDescription(domain.getDescription());
        // category se establece externamente si es necesario
        return entity;
    }

    public List<SubCategoryEntity> toEntity(List<SubCategory> domains) {
        if (domains == null || domains.isEmpty()) {
            return List.of();
        }
        return domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
