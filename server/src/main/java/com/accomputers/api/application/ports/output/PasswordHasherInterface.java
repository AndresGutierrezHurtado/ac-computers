package com.accomputers.api.application.ports.output;

import com.accomputers.api.domain.valueobjects.Password;

public interface PasswordHasherInterface {
    Password hashPassword(Password password);
    boolean verifyPassword(Password password, Password hashedPassword);
}
