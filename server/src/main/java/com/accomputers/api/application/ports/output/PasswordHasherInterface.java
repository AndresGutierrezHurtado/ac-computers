package com.accomputers.api.application.ports.output;

public interface PasswordHasherInterface {
    String hashPassword(String password);
    boolean verifyPassword(String password, String hashedPassword);
}
