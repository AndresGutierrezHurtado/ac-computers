package com.accomputers.api.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.accomputers.api.application.dtos.auth.RegisterDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;
import com.accomputers.api.application.ports.input.AuthServiceInterface;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
public class UserIntegrationTest {

    private final AuthServiceInterface authService;

    private String uniqueEmail() {
        return "john.doe" + System.currentTimeMillis() + "@example.com";
    }

    @Autowired
    public UserIntegrationTest(AuthServiceInterface authService) {
        this.authService = authService;
    }

    @Test
    public void create_user_successfully() {
        String uniqueEmail = uniqueEmail();
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", uniqueEmail, "password", 1);

        UserResponseDTO user = authService.register(registerDTO);

        assertNotNull(user);
        assertNotNull(user.id());
        assertEquals(uniqueEmail, user.email());
    }
}
