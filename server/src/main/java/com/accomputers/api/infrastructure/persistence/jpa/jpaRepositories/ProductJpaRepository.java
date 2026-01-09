package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ProductJpaRepository
                extends JpaRepository<ProductEntity, Integer>, JpaSpecificationExecutor<ProductEntity> {

        // Standard pagination
        @Query("""
                        SELECT DISTINCT p FROM ProductEntity p
                        LEFT JOIN FETCH p.images
                        LEFT JOIN FETCH p.brand
                        LEFT JOIN FETCH p.productSpecifications ps
                        LEFT JOIN FETCH ps.specification
                        LEFT JOIN FETCH ps.specificationValue
                        WHERE p.deletedAt IS NULL
                        """)
        Page<ProductEntity> findAll(Pageable pageable);

        @Query("""
                        SELECT DISTINCT p FROM ProductEntity p
                        LEFT JOIN FETCH p.brand
                        LEFT JOIN FETCH p.productSpecifications ps
                        LEFT JOIN FETCH ps.specification
                        LEFT JOIN FETCH ps.specificationValue
                        WHERE p.id = :id AND p.deletedAt IS NULL
                        """)
        Optional<ProductEntity> findByIdNotDeleted(@Param("id") Integer id);

        // Soft delete by setting deletedAt
        @Modifying
        @Transactional
        @Query("UPDATE ProductEntity p SET p.deletedAt = :deletedAt WHERE p.id = :id")
        void softDelete(@Param("id") Integer id, @Param("deletedAt") LocalDateTime deletedAt);
}
