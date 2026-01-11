package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductSpecificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSpecificationJpaRepository extends JpaRepository<ProductSpecificationEntity, Integer> {
        @Query("SELECT ps FROM ProductSpecificationEntity ps LEFT JOIN FETCH ps.specification WHERE ps.productId = :productId")
        List<ProductSpecificationEntity> findByProductId(@Param("productId") Integer productId);

        @Query("SELECT ps FROM ProductSpecificationEntity ps LEFT JOIN FETCH ps.specification WHERE ps.specificationId = :specificationId")
        List<ProductSpecificationEntity> findBySpecificationId(@Param("specificationId") Integer specificationId);

        @Query("SELECT ps FROM ProductSpecificationEntity ps LEFT JOIN FETCH ps.specification WHERE ps.productId = :productId AND ps.specificationId = :specificationId")
        Optional<ProductSpecificationEntity> findByProductIdAndSpecificationId(@Param("productId") Integer productId,
                        @Param("specificationId") Integer specificationId);

        void deleteByProductId(Integer productId);

        void deleteByProductIdAndSpecificationId(Integer productId, Integer specificationId);
}
