package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.ProductSpecification;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductSpecificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductSpecificationMapper {

    private final SpecificationMapper specificationMapper;
    private final SpecificationValueMapper specificationValueMapper;

    @Autowired
    public ProductSpecificationMapper(SpecificationMapper specificationMapper,
            SpecificationValueMapper specificationValueMapper) {
        this.specificationMapper = specificationMapper;
        this.specificationValueMapper = specificationValueMapper;
    }

    public ProductSpecification toDomain(ProductSpecificationEntity entity) {
        if (entity == null) {
            return null;
        }

        ProductSpecification productSpecification = new ProductSpecification(
                entity.getId(),
                entity.getProductId(),
                entity.getSpecificationId(),
                entity.getValue(),
                entity.getSpecificationValueId());

        // Map relationships using mappers
        if (entity.getSpecification() != null) {
            productSpecification.setSpecification(specificationMapper.toDomain(entity.getSpecification()));
        }
        if (entity.getSpecificationValue() != null) {
            productSpecification
                    .setSpecificationValue(specificationValueMapper.toDomain(entity.getSpecificationValue()));
        }

        return productSpecification;
    }

    public List<ProductSpecification> toDomain(List<ProductSpecificationEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public ProductSpecificationEntity toEntity(ProductSpecification domain) {
        if (domain == null) {
            return null;
        }

        ProductSpecificationEntity entity = new ProductSpecificationEntity();
        entity.setId(domain.getId());
        entity.setProductId(domain.getProductId());
        entity.setSpecificationId(domain.getSpecificationId());
        entity.setValue(domain.getValue());
        entity.setSpecificationValueId(domain.getIdValue());

        return entity;
    }

    public List<ProductSpecificationEntity> toEntity(List<ProductSpecification> domains) {
        if (domains == null || domains.isEmpty()) {
            return List.of();
        }
        return domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
