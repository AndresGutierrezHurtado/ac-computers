package com.accomputers.api.infrastructure.persistence.jpa.mappers;

import com.accomputers.api.domain.entities.Image;
import com.accomputers.api.domain.valueobjects.Url;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ImageEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Image> toDomain(List<ImageEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public ImageEntity toEntity(Image domain) {
        if (domain == null) {
            return null;
        }

        ImageEntity entity = new ImageEntity();
        entity.setId(domain.getId());
        entity.setUrl(domain.getUrl() != null ? domain.getUrl().getValue() : null);
        entity.setIsMain(domain.getIsMain());

        return entity;
    }

    public List<ImageEntity> toEntity(List<Image> domains) {
        if (domains == null || domains.isEmpty()) {
            return List.of();
        }
        return domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

