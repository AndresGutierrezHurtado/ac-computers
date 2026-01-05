package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.SpecificationValue;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SpecificationEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SpecificationValueEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpecificationValueMapper {

    public SpecificationValue toDomain(SpecificationValueEntity entity) {
        if (entity == null) {
            return null;
        }

        return new SpecificationValue(
                entity.getId(),
                entity.getSpecification() != null ? entity.getSpecification().getId() : null,
                entity.getValue(),
                entity.getOrder()
        );
    }

    public List<SpecificationValue> toDomain(List<SpecificationValueEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public SpecificationValueEntity toEntity(SpecificationValue domain) {
        if (domain == null) {
            return null;
        }

        SpecificationValueEntity entity = new SpecificationValueEntity();
        entity.setId(domain.getId());
        entity.setValue(domain.getValue());
        entity.setOrder(domain.getOrder());
        
        if (domain.getSpecificationId() != null) {
            SpecificationEntity specificationEntity = new SpecificationEntity();
            specificationEntity.setId(domain.getSpecificationId());
            entity.setSpecification(specificationEntity);
        }
        
        return entity;
    }

    public List<SpecificationValueEntity> toEntity(List<SpecificationValue> domains) {
        if (domains == null || domains.isEmpty()) {
            return List.of();
        }
        return domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

