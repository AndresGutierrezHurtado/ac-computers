package com.accomputers.api.infrastructure;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.accomputers.api.application.ports.output.PasswordHasherInterface;
import com.accomputers.api.domain.valueobjects.Password;

@Component
public class BcryptHasher implements PasswordHasherInterface {
    @Override
    public Password hashPassword(Password password) {
        return new Password(new BCryptPasswordEncoder().encode(password.getValue()));
    }

    @Override
    public boolean verifyPassword(Password password, Password hashedPassword) {
        return new BCryptPasswordEncoder().matches(password.getValue(), hashedPassword.getValue());
    }
}
