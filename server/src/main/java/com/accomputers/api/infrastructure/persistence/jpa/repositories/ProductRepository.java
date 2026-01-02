package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.Predicate;

// Domain
import com.accomputers.api.domain.entities.Product;
import com.accomputers.api.domain.valueobjects.Condition;

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
        Specification<ProductEntity> spec = buildSpecification(productCriteria);
        Page<ProductEntity> page = jpaRepository.findAll(spec, pageable);
        List<Product> products = page.getContent().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
        return new PageDTO<>(products, page.getTotalElements());
    }

    private Specification<ProductEntity> buildSpecification(ProductCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always exclude soft-deleted products
            predicates.add(cb.isNull(root.get("deletedAt")));

            // Search filter (name or description)
            if (criteria.getSearch() != null && !criteria.getSearch().trim().isEmpty()) {
                String searchTerm = "%" + criteria.getSearch().toLowerCase() + "%";
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), searchTerm);
                Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), searchTerm);
                predicates.add(cb.or(namePredicate, descriptionPredicate));
            }

            // Category filter (through subCategory)
            if (criteria.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("subCategory").get("category").get("id"), criteria.getCategoryId()));
            }

            // SubCategory filter
            if (criteria.getSubCategoryId() != null) {
                predicates.add(cb.equal(root.get("subCategory").get("id"), criteria.getSubCategoryId()));
            }

            // Condition filter
            if (criteria.getCondition() != null && !criteria.getCondition().trim().isEmpty()) {
                Condition.ConditionType conditionType = Condition.ConditionType.fromString(criteria.getCondition());
                if (conditionType != null) {
                    ProductEntity.ConditionType entityCondition = ProductEntity.ConditionType.valueOf(conditionType.name());
                    predicates.add(cb.equal(root.get("condition"), entityCondition));
                }
            }

            // Brand filter
            if (criteria.getBrandId() != null) {
                predicates.add(cb.equal(root.get("brand").get("id"), criteria.getBrandId()));
            }

            // Price range filters
            if (criteria.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
            }
            if (criteria.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
            }

            // Discount range filters
            if (criteria.getMinDiscount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("discount"), criteria.getMinDiscount()));
            }
            if (criteria.getMaxDiscount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("discount"), criteria.getMaxDiscount()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
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

