package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.Brand;
import com.accomputers.api.domain.entities.Image;
import com.accomputers.api.domain.entities.Product;
import com.accomputers.api.domain.valueobjects.Condition;
import com.accomputers.api.domain.valueobjects.Discount;
import com.accomputers.api.domain.valueobjects.Price;
import com.accomputers.api.domain.valueobjects.Url;
import com.accomputers.api.infrastructure.persistence.jpa.entities.BrandEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ImageEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.SubCategoryEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

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

        if (entity.getBrand() != null) {
            try {
                Constructor<Brand> constructor = Brand.class.getDeclaredConstructor(String.class, String.class);
                constructor.setAccessible(true);
                Brand brand = constructor.newInstance(
                    entity.getBrand().getId(),
                    entity.getBrand().getName()
                );
                product.setBrand(brand);
            } catch (Exception e) {
                // If reflection fails, skip setting Brand
            }
        }

        if (entity.getImages() != null && !entity.getImages().isEmpty()) {
            List<Image> images = entity.getImages().stream()
                .map(this::imageToDomain)
                .collect(Collectors.toList());
            product.setImages(images);
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

        if (domain.getBrand() != null && domain.getBrand().getId() != null) {
            BrandEntity brandEntity = new BrandEntity();
            brandEntity.setId(domain.getBrand().getId());
            brandEntity.setName(domain.getBrand().getName());
            entity.setBrand(brandEntity);
        } else if (domain.getBrandId() != null) {
            BrandEntity brandEntity = new BrandEntity();
            brandEntity.setId(domain.getBrandId());
            entity.setBrand(brandEntity);
        }

        if (domain.getSubCategoryId() != null) {
            SubCategoryEntity subCategoryEntity = new SubCategoryEntity();
            subCategoryEntity.setId(domain.getSubCategoryId());
            entity.setSubCategory(subCategoryEntity);
        }

        if (domain.getImages() != null && !domain.getImages().isEmpty()) {
            List<ImageEntity> imageEntities = domain.getImages().stream()
                .map(img -> imageToEntity(img, entity))
                .collect(Collectors.toList());
            entity.setImages(imageEntities);
        }

        return entity;
    }

    private Image imageToDomain(ImageEntity entity) {
        if (entity == null) {
            return null;
        }

        Image image = new Image(
            entity.getId(),
            new Url(entity.getUrl()),
            entity.getIsMain(),
            entity.getProduct() != null ? entity.getProduct().getId() : null
        );

        return image;
    }

    private ImageEntity imageToEntity(Image domain, ProductEntity productEntity) {
        if (domain == null) {
            return null;
        }

        ImageEntity entity = new ImageEntity();
        entity.setId(domain.getId());
        entity.setUrl(domain.getUrl() != null ? domain.getUrl().getValue() : null);
        entity.setIsMain(domain.getIsMain());
        entity.setProduct(productEntity);

        return entity;
    }
}

