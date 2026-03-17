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
import java.util.Collection;
import java.util.List;
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

        @Query("SELECT p.id FROM ProductEntity p WHERE p.deletedAt IS NULL AND p.embedding IS NULL ORDER BY p.id")
        List<Integer> findActiveIdsWithMissingEmbedding();

        @Modifying(clearAutomatically = true, flushAutomatically = true)
        @Transactional
        @Query(value = """
                        UPDATE products
                        SET embedding = CAST(:vec AS vector), updated_at = NOW()
                        WHERE id = :id AND deleted_at IS NULL
                        """, nativeQuery = true)
        void updateEmbeddingVectorById(@Param("id") Integer id, @Param("vec") String vec);

        @Query("""
                        SELECT DISTINCT p FROM ProductEntity p
                        LEFT JOIN FETCH p.brand
                        LEFT JOIN FETCH p.productSpecifications ps
                        LEFT JOIN FETCH ps.specification
                        LEFT JOIN FETCH ps.specificationValue
                        WHERE p.id IN :ids AND p.deletedAt IS NULL
                        """)
        List<ProductEntity> findAllByIdInWithAssociations(@Param("ids") Collection<Integer> ids);
}
