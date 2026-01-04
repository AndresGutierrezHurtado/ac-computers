package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.Image;
import com.accomputers.api.domain.valueobjects.Url;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ImageEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {

    public Image toDomain(ImageEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Image(
            entity.getId(),
            new Url(entity.getUrl()),
            entity.getIsMain(),
            entity.getProduct() != null ? entity.getProduct().getId() : null
        );
    }

    public ImageEntity toEntity(Image domain, ProductEntity productEntity) {
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

