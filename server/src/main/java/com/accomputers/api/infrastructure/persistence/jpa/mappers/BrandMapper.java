package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.Brand;
import com.accomputers.api.infrastructure.persistence.jpa.entities.BrandEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BrandMapper {

    public Brand toDomain(BrandEntity entity) {
        if (entity == null) {
            return null;
        }

        Brand brand = new Brand(entity.getId(), entity.getName());

        return brand;
    }

    public List<Brand> toDomain(List<BrandEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public BrandEntity toEntity(Brand domain) {
        if (domain == null) {
            return null;
        }

        BrandEntity entity = new BrandEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());

        return entity;
    }

    public List<BrandEntity> toEntity(List<Brand> domains) {
        if (domains == null || domains.isEmpty()) {
            return List.of();
        }
        return domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
