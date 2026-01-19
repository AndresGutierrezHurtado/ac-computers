package com.accomputers.api.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.accomputers.api.application.dtos.auth.LoginDTO;
import com.accomputers.api.application.dtos.auth.RegisterDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;
import com.accomputers.api.application.ports.input.AuthServiceInterface;
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.exceptions.InvalidValueObjectException;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
public class AuthIntegrationTest {

    private final AuthServiceInterface authService;

    private String uniqueEmail() {
        return "test.user" + System.currentTimeMillis() + "@example.com";
    }

    @Autowired
    public AuthIntegrationTest(AuthServiceInterface authService) {
        this.authService = authService;
    }

    @Test
    public void login_successfully() {
        String uniqueEmail = uniqueEmail();
        String password = "password123";
        
        // First register a user
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", uniqueEmail, password, 1);
        UserResponseDTO registeredUser = authService.register(registerDTO);

        // Then try to login
        LoginDTO loginDTO = new LoginDTO(uniqueEmail, password);
        UserResponseDTO loggedInUser = authService.login(loginDTO);

        assertNotNull(loggedInUser);
        assertEquals(registeredUser.id(), loggedInUser.id());
        assertEquals(registeredUser.email(), loggedInUser.email());
        assertEquals(registeredUser.firstName(), loggedInUser.firstName());
        assertEquals(registeredUser.lastName(), loggedInUser.lastName());
    }

    @Test
    public void login_with_incorrect_password() {
        String uniqueEmail = uniqueEmail();
        String correctPassword = "password123";
        String incorrectPassword = "wrongpassword";
        
        // First register a user
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", uniqueEmail, correctPassword, 1);
        authService.register(registerDTO);

        // Try to login with wrong password
        LoginDTO loginDTO = new LoginDTO(uniqueEmail, incorrectPassword);
        
        assertThrows(InvalidValueObjectException.class, () -> authService.login(loginDTO));
    }

    @Test
    public void login_with_nonexistent_user() {
        LoginDTO loginDTO = new LoginDTO("nonexistent@example.com", "password123");
        
        assertThrows(EntityNotFoundException.class, () -> authService.login(loginDTO));
    }

    @Test
    public void login_with_invalid_email() {
        LoginDTO loginDTO = new LoginDTO("invalid-email", "password123");
        
        assertThrows(InvalidValueObjectException.class, () -> authService.login(loginDTO));
    }
}
