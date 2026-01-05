package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import com.accomputers.api.application.ports.output.repositories.ImageRepositoryInterface;
import com.accomputers.api.domain.entities.Image;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ImageEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.ImageJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.ImageMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ImageRepository implements ImageRepositoryInterface {

    private final ImageJpaRepository jpaRepository;
    private final ImageMapper mapper;

    public ImageRepository(ImageJpaRepository jpaRepository, ImageMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Image save(Image image) {
        if (image == null) {
            return null;
        }

        ImageEntity entity = mapper.toEntity(image);
        if (image.getProductId() != null) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(image.getProductId());
            entity.setProduct(productEntity);
        }
        ImageEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<Image> findByProductId(Integer productId) {
        List<ImageEntity> entities = jpaRepository.findByProductId(productId);
        return entities.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteByProductId(Integer productId) {
        jpaRepository.deleteByProductId(productId);
    }

    @Override
    public void delete(Integer id) {
        jpaRepository.deleteById(id);
    }
}

