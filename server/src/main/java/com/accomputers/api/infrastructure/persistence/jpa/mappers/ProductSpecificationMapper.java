package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.ProductSpecification;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductSpecificationEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SpecificationEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SpecificationValueEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductSpecificationMapper {

    public ProductSpecification toDomain(ProductSpecificationEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ProductSpecification(
            entity.getProduct() != null ? entity.getProduct().getId() : null,
            entity.getSpecification() != null ? entity.getSpecification().getId() : null,
            entity.getValue(),
            entity.getSpecificationValue() != null ? entity.getSpecificationValue().getId() : null
        );
    }

    public ProductSpecificationEntity toEntity(ProductSpecification domain, ProductEntity productEntity) {
        if (domain == null) {
            return null;
        }

        ProductSpecificationEntity entity = new ProductSpecificationEntity();
        entity.setId(null); // New entity, no ID yet
        entity.setProduct(productEntity);

        if (domain.getSpecificationId() != null) {
            SpecificationEntity specificationEntity = new SpecificationEntity();
            specificationEntity.setId(domain.getSpecificationId());
            entity.setSpecification(specificationEntity);
        }

        entity.setValue(domain.getValue());

        if (domain.getIdValue() != null) {
            SpecificationValueEntity specificationValueEntity = new SpecificationValueEntity();
            specificationValueEntity.setId(domain.getIdValue());
            entity.setSpecificationValue(specificationValueEntity);
        }

        return entity;
    }
}

