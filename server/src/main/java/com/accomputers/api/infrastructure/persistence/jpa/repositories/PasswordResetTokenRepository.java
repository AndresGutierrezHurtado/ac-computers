package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import org.springframework.stereotype.Repository;

import com.accomputers.api.application.ports.output.repositories.PasswordResetTokenRepositoryInterface;
import com.accomputers.api.domain.entities.PasswordResetToken;
import com.accomputers.api.infrastructure.persistence.jpa.entities.PasswordResetTokenEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.PasswordResetTokenJpaRepository;

@Repository
public class PasswordResetTokenRepository implements PasswordResetTokenRepositoryInterface {
    private final PasswordResetTokenJpaRepository jpaRepository;

    public PasswordResetTokenRepository(PasswordResetTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        return jpaRepository.findByToken(token).map(this::toDomain).orElse(null);
    }

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        if (passwordResetToken == null) {
            return null;
        }
        PasswordResetTokenEntity entity = toEntity(passwordResetToken);
        PasswordResetTokenEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    private PasswordResetToken toDomain(PasswordResetTokenEntity entity) {
        if (entity == null) {
            return null;
        }
        return new PasswordResetToken(
                entity.getId(),
                entity.getUserId(),
                entity.getToken(),
                entity.getExpiresAt(),
                entity.getUsedAt());
    }

    private PasswordResetTokenEntity toEntity(PasswordResetToken domain) {
        if (domain == null) {
            return null;
        }
        return new PasswordResetTokenEntity(
                domain.getId(),
                domain.getUserId(),
                domain.getToken(),
                domain.getExpiresAt(),
                domain.getUsedAt());
    }
}
