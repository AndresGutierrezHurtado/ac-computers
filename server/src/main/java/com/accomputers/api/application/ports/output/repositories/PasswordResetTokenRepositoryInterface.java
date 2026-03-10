package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.domain.entities.PasswordResetToken;

public interface PasswordResetTokenRepositoryInterface {
    PasswordResetToken findByToken(String token);
    PasswordResetToken save(PasswordResetToken passwordResetToken);
}
