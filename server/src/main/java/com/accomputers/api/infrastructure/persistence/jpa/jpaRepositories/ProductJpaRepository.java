package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Integer> {
    
    // Soft delete queries
    @Query("SELECT p FROM ProductEntity p WHERE p.deletedAt IS NULL")
    Page<ProductEntity> findAllNotDeleted(Pageable pageable);
    
    @Query("SELECT p FROM ProductEntity p WHERE p.deletedAt IS NULL")
    List<ProductEntity> findAllNotDeleted();
    
    @Query("SELECT p FROM ProductEntity p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<ProductEntity> findByIdNotDeleted(@Param("id") Integer id);
    
    // Soft delete by setting deletedAt
    @Modifying
    @Transactional
    @Query("UPDATE ProductEntity p SET p.deletedAt = :deletedAt WHERE p.id = :id")
    void softDelete(@Param("id") Integer id, @Param("deletedAt") LocalDateTime deletedAt);
    
    // Find by brand
    @Query("SELECT p FROM ProductEntity p WHERE p.brand.id = :brandId AND p.deletedAt IS NULL")
    Page<ProductEntity> findByBrandIdNotDeleted(@Param("brandId") String brandId, Pageable pageable);
    
    // Find by subcategory
    @Query("SELECT p FROM ProductEntity p WHERE p.subCategory.id = :subCategoryId AND p.deletedAt IS NULL")
    Page<ProductEntity> findBySubCategoryIdNotDeleted(@Param("subCategoryId") Integer subCategoryId, Pageable pageable);
    
    // Find by condition
    @Query("SELECT p FROM ProductEntity p WHERE p.condition = :condition AND p.deletedAt IS NULL")
    Page<ProductEntity> findByConditionNotDeleted(@Param("condition") ProductEntity.ConditionType condition, Pageable pageable);
    
    // Search by name
    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.deletedAt IS NULL")
    Page<ProductEntity> findByNameContainingNotDeleted(@Param("name") String name, Pageable pageable);
    
    // Standard pagination (includes deleted)
    Page<ProductEntity> findAll(Pageable pageable);
}

