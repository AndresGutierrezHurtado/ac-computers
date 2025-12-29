package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandJpaRepository extends JpaRepository<BrandEntity, String> {
    Optional<BrandEntity> findByName(String name);
    boolean existsByName(String name);
}

