package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryJpaRepository extends JpaRepository<SubCategoryEntity, Integer> {
    Optional<SubCategoryEntity> findBySlug(String slug);
    Optional<SubCategoryEntity> findByName(String name);
    List<SubCategoryEntity> findByCategoryId(Integer categoryId);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
}

