package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.Product;
import com.accomputers.api.domain.valueobjects.Condition;
import com.accomputers.api.domain.valueobjects.Discount;
import com.accomputers.api.domain.valueobjects.Price;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SubCategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    private final BrandMapper brandMapper;
    private final ImageMapper imageMapper;
    private final ProductSpecificationMapper productSpecificationMapper;

    @Autowired
    public ProductMapper(BrandMapper brandMapper, ImageMapper imageMapper, ProductSpecificationMapper productSpecificationMapper) {
        this.brandMapper = brandMapper;
        this.imageMapper = imageMapper;
        this.productSpecificationMapper = productSpecificationMapper;
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
            new Condition(entity.getCondition() != null ? entity.getCondition().name().toLowerCase() : "new"),
            new Discount(entity.getDiscount()),
            null, // brandId - cannot convert String to Integer, use Brand object instead
            entity.getSubCategory() != null ? entity.getSubCategory().getId() : null,
            entity.getDeletedAt(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );

        // Mapear relaciones usando mappers
        if (entity.getBrand() != null) {
            product.setBrand(brandMapper.toDomain(entity.getBrand()));
            product.setBrandId(entity.getBrand().getId());
        }

        if (entity.getImages() != null && !entity.getImages().isEmpty()) {
            product.setImages(imageMapper.toDomain(entity.getImages()));
        }

        if (entity.getProductSpecifications() != null && !entity.getProductSpecifications().isEmpty()) {
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
        
        if (domain.getCondition() != null) {
            ProductEntity.ConditionType conditionType = ProductEntity.ConditionType.valueOf(
                domain.getCondition().getConditionType().name()
            );
            entity.setCondition(conditionType);
        }
        
        entity.setDiscount(domain.getDiscount() != null ? domain.getDiscount().getValue() : null);
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        // Mapear relaciones usando mappers
        if (domain.getBrand() != null) {
            entity.setBrand(brandMapper.toEntity(domain.getBrand()));
        } else if (domain.getBrandId() != null) {
            // Crear BrandEntity directamente cuando solo tenemos el ID
            com.accomputers.api.infrastructure.persistence.jpa.entities.BrandEntity brandEntity = 
                new com.accomputers.api.infrastructure.persistence.jpa.entities.BrandEntity();
            brandEntity.setId(domain.getBrandId());
            entity.setBrand(brandEntity);
        }

        if (domain.getSubCategoryId() != null) {
            SubCategoryEntity subCategoryEntity = new SubCategoryEntity();
            subCategoryEntity.setId(domain.getSubCategoryId());
            entity.setSubCategory(subCategoryEntity);
        }

        if (domain.getImages() != null && !domain.getImages().isEmpty()) {
            List<com.accomputers.api.infrastructure.persistence.jpa.entities.ImageEntity> imageEntities = 
                imageMapper.toEntity(domain.getImages());
            // Establecer la relación con el producto
            imageEntities.forEach(img -> img.setProduct(entity));
            entity.setImages(imageEntities);
        }

        if (domain.getProductSpecifications() != null && !domain.getProductSpecifications().isEmpty()) {
            List<com.accomputers.api.infrastructure.persistence.jpa.entities.ProductSpecificationEntity> specEntities = 
                productSpecificationMapper.toEntity(domain.getProductSpecifications());
            // Establecer la relación con el producto
            specEntities.forEach(spec -> spec.setProduct(entity));
            entity.setProductSpecifications(specEntities);
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

