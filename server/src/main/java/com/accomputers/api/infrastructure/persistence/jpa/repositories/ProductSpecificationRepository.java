package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import com.accomputers.api.application.ports.output.repositories.ProductSpecificationRepositoryInterface;
import com.accomputers.api.domain.entities.ProductSpecification;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductSpecificationEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.ProductSpecificationJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.ProductSpecificationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductSpecificationRepository implements ProductSpecificationRepositoryInterface {

    private final ProductSpecificationJpaRepository jpaRepository;
    private final ProductSpecificationMapper mapper;

    public ProductSpecificationRepository(
            ProductSpecificationJpaRepository jpaRepository,
            ProductSpecificationMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ProductSpecification save(ProductSpecification productSpecification) {
        if (productSpecification == null) {
            return null;
        }

        ProductEntity productEntity = null;
        if (productSpecification.getProductId() != null) {
            productEntity = new ProductEntity();
            productEntity.setId(productSpecification.getProductId());
        }

        ProductSpecificationEntity entity = mapper.toEntity(productSpecification, productEntity);
        ProductSpecificationEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<ProductSpecification> findByProductId(Integer productId) {
        List<ProductSpecificationEntity> entities = jpaRepository.findByProductId(productId);
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

