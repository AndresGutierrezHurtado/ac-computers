package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.role WHERE u.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);
    boolean existsByEmail(String email);
    Page<UserEntity> findAll(Pageable pageable);
}

