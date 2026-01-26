package com.accomputers.api.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.accomputers.api.application.dtos.auth.RegisterDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;
import com.accomputers.api.application.ports.input.AuthServiceInterface;
import com.accomputers.api.domain.exceptions.EmailAlreadyExistsException;
import com.accomputers.api.domain.exceptions.InvalidValueObjectException;

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

    @Test
    public void create_user_with_existing_email() {
        String uniqueEmail = uniqueEmail();
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", uniqueEmail, "password", 1);
        authService.register(registerDTO);

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_invalid_email() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "invalid-email", "password", 1);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_empty_email() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "", "password", 1);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_null_email() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", null, "password", 1);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_email_missing_at_symbol() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "johndoeexample.com", "password", 1);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_email_missing_domain() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "johndoe@", "password", 1);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_email_missing_username() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "@example.com", "password", 1);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_invalid_password() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", "123", 1);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_empty_password() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", "", 1);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_null_password() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", null, 1);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_password_too_short() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", "abc", 1);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_invalid_role_id_negative() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", "password", -1);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_invalid_role_id_zero() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", "password", 0);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }

    @Test
    public void create_user_with_nonexistent_role_id() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", "password", 99999);
        assertThrows(InvalidValueObjectException.class, () -> authService.register(registerDTO));
    }
}
