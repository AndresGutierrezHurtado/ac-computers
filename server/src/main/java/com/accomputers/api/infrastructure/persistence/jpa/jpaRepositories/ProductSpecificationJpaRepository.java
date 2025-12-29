package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductSpecificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSpecificationJpaRepository extends JpaRepository<ProductSpecificationEntity, Integer> {
    List<ProductSpecificationEntity> findByProductId(Integer productId);
    List<ProductSpecificationEntity> findBySpecificationId(Integer specificationId);
    Optional<ProductSpecificationEntity> findByProductIdAndSpecificationId(Integer productId, Integer specificationId);
    void deleteByProductId(Integer productId);
    void deleteByProductIdAndSpecificationId(Integer productId, Integer specificationId);
}

