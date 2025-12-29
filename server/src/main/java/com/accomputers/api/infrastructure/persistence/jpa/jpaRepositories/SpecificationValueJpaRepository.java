package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.SpecificationValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecificationValueJpaRepository extends JpaRepository<SpecificationValueEntity, Integer> {
    List<SpecificationValueEntity> findBySpecificationId(Integer specificationId);
    
    @Query("SELECT sv FROM SpecificationValueEntity sv WHERE sv.specification.id = :specificationId ORDER BY sv.order ASC")
    List<SpecificationValueEntity> findBySpecificationIdOrderByOrderAsc(@Param("specificationId") Integer specificationId);
    
    Optional<SpecificationValueEntity> findBySpecificationIdAndValue(Integer specificationId, String value);
}

