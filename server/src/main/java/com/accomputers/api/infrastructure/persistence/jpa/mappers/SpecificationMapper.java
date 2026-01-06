package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.Specification;
import com.accomputers.api.domain.valueobjects.Slug;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SpecificationEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpecificationMapper {

    public Specification toDomain(SpecificationEntity entity) {
        if (entity == null) {
            return null;
        }

        Specification specification = new Specification(
                entity.getId(),
                entity.getName(),
                new Slug(entity.getSlug()),
                entity.getType(),
                entity.getUnit(),
                entity.getIsFilterable(),
                entity.getIsMandatory(),
                entity.getSubCategoryId());

        return specification;
    }

    public List<Specification> toDomain(List<SpecificationEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public SpecificationEntity toEntity(Specification domain) {
        if (domain == null) {
            return null;
        }

        SpecificationEntity entity = new SpecificationEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setSlug(domain.getSlug() != null ? domain.getSlug().getValue() : null);
        entity.setType(domain.getType());
        entity.setUnit(domain.getUnit());
        entity.setIsFilterable(domain.getIsFilterable());
        entity.setIsMandatory(domain.getIsMandatory());
        entity.setSubCategoryId(domain.getSubCategoryId());

        return entity;
    }

    public List<SpecificationEntity> toEntity(List<Specification> domains) {
        if (domains == null || domains.isEmpty()) {
            return List.of();
        }
        return domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
