package com.accomputers.api.Integration;

// JUnit
import org.junit.jupiter.api.Test;

// Assertions
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

// DTOs and Ports
import com.accomputers.api.application.dtos.ContactDTO;
import com.accomputers.api.application.ports.input.ContactServiceInterface;

// Transactional
import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
public class ContactIntegrationTest {

    private final ContactServiceInterface contactService;

    @Autowired
    public ContactIntegrationTest(ContactServiceInterface contactService) {
        this.contactService = contactService;
    }

    @Test
    public void send_contact_feedback_successfully() {
        ContactDTO contactDTO = new ContactDTO(
                "Test Subject",
                "John Doe",
                "john.doe@example.com",
                "This is a test message for contact feedback."
        );

        assertDoesNotThrow(() -> contactService.sendContactFeedback(contactDTO));
    }
}
