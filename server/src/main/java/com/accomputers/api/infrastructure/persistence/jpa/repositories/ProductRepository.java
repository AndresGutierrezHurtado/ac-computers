package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import com.accomputers.api.application.ports.output.repositories.ProductRepositoryInterface;
import com.accomputers.api.domain.entities.Product;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.ProductJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.ProductMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository implements ProductRepositoryInterface {

    private final ProductJpaRepository jpaRepository;
    private final ProductMapper mapper;

    public ProductRepository(ProductJpaRepository jpaRepository, ProductMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAllNotDeleted().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Product findById(Integer id) {
        return jpaRepository.findByIdNotDeleted(id)
            .map(mapper::toDomain)
            .orElse(null);
    }

    @Override
    public Product save(Product product) {
        if (product == null) {
            return null;
        }

        ProductEntity entity = mapper.toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Integer id) {
        jpaRepository.softDelete(id, LocalDateTime.now());
    }
}

