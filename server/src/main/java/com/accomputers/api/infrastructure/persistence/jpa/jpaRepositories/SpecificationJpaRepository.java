package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.SpecificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecificationJpaRepository extends JpaRepository<SpecificationEntity, Integer> {
    Optional<SpecificationEntity> findBySlug(String slug);
    Optional<SpecificationEntity> findByName(String name);
    List<SpecificationEntity> findBySubCategoryId(Integer subCategoryId);
    List<SpecificationEntity> findBySubCategoryIdAndIsFilterableTrue(Integer subCategoryId);
    List<SpecificationEntity> findBySubCategoryIdAndIsMandatoryTrue(Integer subCategoryId);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
}

