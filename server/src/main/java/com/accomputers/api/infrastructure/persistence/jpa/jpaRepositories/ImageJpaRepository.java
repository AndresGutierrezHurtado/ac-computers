package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageJpaRepository extends JpaRepository<ImageEntity, Integer> {
    List<ImageEntity> findByProductId(Integer productId);
    Optional<ImageEntity> findByProductIdAndIsMainTrue(Integer productId);
    void deleteByProductId(Integer productId);

    @Modifying
    @Transactional
    @Query("UPDATE ImageEntity i SET i.isMain = false WHERE i.productId = :productId")
    void setAllImagesAsNotMainByProductId(@Param("productId") Integer productId);
}

