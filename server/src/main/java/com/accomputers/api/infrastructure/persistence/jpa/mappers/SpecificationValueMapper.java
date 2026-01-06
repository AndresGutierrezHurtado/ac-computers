package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.SpecificationValue;
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

        SpecificationValue specificationValue = new SpecificationValue(
                entity.getId(),
                entity.getSpecificationId() != null ? entity.getSpecificationId() : (entity.getSpecification() != null ? entity.getSpecification().getId() : null),
                entity.getValue(),
                entity.getOrder()
        );

        return specificationValue;
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
        entity.setSpecificationId(domain.getSpecificationId());
        entity.setValue(domain.getValue());
        entity.setOrder(domain.getOrder());

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

