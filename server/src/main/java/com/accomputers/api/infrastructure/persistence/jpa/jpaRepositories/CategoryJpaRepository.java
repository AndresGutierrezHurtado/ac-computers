package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Integer> {
    Optional<CategoryEntity> findBySlug(String slug);
    Optional<CategoryEntity> findByName(String name);
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
}

