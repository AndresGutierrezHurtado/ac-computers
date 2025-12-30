package com.accomputers.api.infrastructure;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.accomputers.api.application.ports.output.PasswordHasherInterface;

@Component
public class BcryptHasher implements PasswordHasherInterface {
    @Override
    public String hashPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public boolean verifyPassword(String password, String hashedPassword) {
        return new BCryptPasswordEncoder().matches(password, hashedPassword);
    }
}
