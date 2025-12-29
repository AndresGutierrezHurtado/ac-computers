package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageJpaRepository extends JpaRepository<ImageEntity, Integer> {
    List<ImageEntity> findByProductId(Integer productId);
    Optional<ImageEntity> findByProductIdAndIsMainTrue(Integer productId);
    void deleteByProductId(Integer productId);
}

