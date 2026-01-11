package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.Product;
import com.accomputers.api.domain.valueobjects.Condition;
import com.accomputers.api.domain.valueobjects.Discount;
import com.accomputers.api.domain.valueobjects.Price;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    private final BrandMapper brandMapper;
    private final ImageMapper imageMapper;
    private final ProductSpecificationMapper productSpecificationMapper;
    private final SubCategoryMapper subCategoryMapper;

    @Autowired
    public ProductMapper(BrandMapper brandMapper, ImageMapper imageMapper,
            ProductSpecificationMapper productSpecificationMapper, SubCategoryMapper subCategoryMapper) {
        this.brandMapper = brandMapper;
        this.imageMapper = imageMapper;
        this.productSpecificationMapper = productSpecificationMapper;
        this.subCategoryMapper = subCategoryMapper;
    }

    public Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        Product product = new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                new Price(entity.getPrice()),
                new Condition(entity.getCondition().name()),
                new Discount(entity.getDiscount()),
                entity.getBrandId(),
                entity.getSubCategoryId(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());

        // Mapear relaciones usando mappers
        if (entity.getBrand() != null) {
            product.setBrand(brandMapper.toDomain(entity.getBrand()));
        }

        if (entity.getSubCategory() != null) {
            product.setSubCategory(subCategoryMapper.toDomain(entity.getSubCategory()));
        }

        if (entity.getImages() != null) {
            product.setImages(imageMapper.toDomain(entity.getImages()));
        }

        if (entity.getProductSpecifications() != null) {
            product.setProductSpecifications(productSpecificationMapper.toDomain(entity.getProductSpecifications()));
        }

        return product;
    }

    public ProductEntity toEntity(Product domain) {
        if (domain == null) {
            return null;
        }

        ProductEntity entity = new ProductEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice() != null ? domain.getPrice().getValue() : null);
        entity.setDiscount(domain.getDiscount() != null ? domain.getDiscount().getValue() : null);
        entity.setBrandId(domain.getBrandId());
        entity.setSubCategoryId(domain.getSubCategoryId());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        if (domain.getCondition() != null) {
            ProductEntity.ConditionType conditionType = ProductEntity.ConditionType.valueOf(
                    domain.getCondition().getConditionType().name());
            entity.setCondition(conditionType);
        }

        if (domain.getImages() != null) {
            entity.setImages(imageMapper.toEntity(domain.getImages()));
        }

        if (domain.getProductSpecifications() != null) {
            entity.setProductSpecifications(productSpecificationMapper.toEntity(domain.getProductSpecifications()));
        }

        return entity;
    }

    public List<Product> toDomain(List<ProductEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public List<ProductEntity> toEntity(List<Product> domains) {
        if (domains == null || domains.isEmpty()) {
            return List.of();
        }
        return domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
