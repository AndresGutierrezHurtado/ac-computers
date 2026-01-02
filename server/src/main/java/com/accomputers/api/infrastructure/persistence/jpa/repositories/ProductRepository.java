package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

// Domain
import com.accomputers.api.domain.entities.Product;

// Application
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.ProductCriteria;
import com.accomputers.api.application.ports.output.repositories.ProductRepositoryInterface;

// Infrastructure
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.ProductJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.ProductMapper;

@Repository
public class ProductRepository implements ProductRepositoryInterface {

    private final ProductJpaRepository jpaRepository;
    private final ProductMapper mapper;

    public ProductRepository(ProductJpaRepository jpaRepository, ProductMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public PageDTO<Product> findAll(ProductCriteria productCriteria) {
        Pageable pageable = PageRequest.of(productCriteria.getPage() - 1, productCriteria.getPerPage());
        Page<ProductEntity> page = jpaRepository.findAllNotDeleted(pageable);
        List<Product> products = page.getContent().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
        return new PageDTO<>(products, page.getTotalElements());
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

