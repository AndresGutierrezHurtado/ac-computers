package com.accomputers.api.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.accomputers.api.application.dtos.auth.LoginDTO;
import com.accomputers.api.application.dtos.auth.RegisterDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;
import com.accomputers.api.application.ports.input.AuthServiceInterface;

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
}
