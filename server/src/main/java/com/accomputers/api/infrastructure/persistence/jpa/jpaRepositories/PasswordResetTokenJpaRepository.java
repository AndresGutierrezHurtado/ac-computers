package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accomputers.api.infrastructure.persistence.jpa.entities.PasswordResetTokenEntity;

@Repository
public interface PasswordResetTokenJpaRepository extends JpaRepository<PasswordResetTokenEntity, Integer> {
    Optional<PasswordResetTokenEntity> findByToken(String token);
}
