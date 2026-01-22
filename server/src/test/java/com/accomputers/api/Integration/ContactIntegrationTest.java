package com.accomputers.api.Integration;

// JUnit
import org.junit.jupiter.api.Test;

// Assertions
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

// DTOs and Ports
import com.accomputers.api.application.dtos.ContactDTO;
import com.accomputers.api.application.ports.input.ContactServiceInterface;

// Domain
import com.accomputers.api.domain.exceptions.InvalidValueObjectException;

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

    @Test
    public void send_contact_feedback_with_empty_subject() {
        ContactDTO contactDTO = new ContactDTO(
                "",
                "John Doe",
                "john.doe@example.com",
                "This is a test message."
        );

        assertThrows(InvalidValueObjectException.class, () -> contactService.sendContactFeedback(contactDTO));
    }

    @Test
    public void send_contact_feedback_with_null_subject() {
        ContactDTO contactDTO = new ContactDTO(
                null,
                "John Doe",
                "john.doe@example.com",
                "This is a test message."
        );

        assertThrows(InvalidValueObjectException.class, () -> contactService.sendContactFeedback(contactDTO));
    }

    @Test
    public void send_contact_feedback_with_empty_name() {
        ContactDTO contactDTO = new ContactDTO(
                "Test Subject",
                "",
                "john.doe@example.com",
                "This is a test message."
        );

        assertThrows(InvalidValueObjectException.class, () -> contactService.sendContactFeedback(contactDTO));
    }

    @Test
    public void send_contact_feedback_with_null_name() {
        ContactDTO contactDTO = new ContactDTO(
                "Test Subject",
                null,
                "john.doe@example.com",
                "This is a test message."
        );

        assertThrows(InvalidValueObjectException.class, () -> contactService.sendContactFeedback(contactDTO));
    }

    @Test
    public void send_contact_feedback_with_empty_email() {
        ContactDTO contactDTO = new ContactDTO(
                "Test Subject",
                "John Doe",
                "",
                "This is a test message."
        );

        assertThrows(InvalidValueObjectException.class, () -> contactService.sendContactFeedback(contactDTO));
    }

    @Test
    public void send_contact_feedback_with_null_email() {
        ContactDTO contactDTO = new ContactDTO(
                "Test Subject",
                "John Doe",
                null,
                "This is a test message."
        );

        assertThrows(InvalidValueObjectException.class, () -> contactService.sendContactFeedback(contactDTO));
    }

    @Test
    public void send_contact_feedback_with_invalid_email() {
        ContactDTO contactDTO = new ContactDTO(
                "Test Subject",
                "John Doe",
                "invalid-email",
                "This is a test message."
        );

        assertThrows(InvalidValueObjectException.class, () -> contactService.sendContactFeedback(contactDTO));
    }

    @Test
    public void send_contact_feedback_with_empty_message() {
        ContactDTO contactDTO = new ContactDTO(
                "Test Subject",
                "John Doe",
                "john.doe@example.com",
                ""
        );

        assertThrows(InvalidValueObjectException.class, () -> contactService.sendContactFeedback(contactDTO));
    }

    @Test
    public void send_contact_feedback_with_null_message() {
        ContactDTO contactDTO = new ContactDTO(
                "Test Subject",
                "John Doe",
                "john.doe@example.com",
                null
        );

        assertThrows(InvalidValueObjectException.class, () -> contactService.sendContactFeedback(contactDTO));
    }
}
