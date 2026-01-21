package com.accomputers.api.application.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Ports
import com.accomputers.api.application.ports.input.ContactServiceInterface;
import com.accomputers.api.application.ports.output.LoggerPort;
import com.accomputers.api.application.ports.output.MessagingService;
import com.accomputers.api.domain.exceptions.InvalidValueObjectException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

// DTOs
import com.accomputers.api.application.dtos.ContactDTO;

@Service
public class ContactService implements ContactServiceInterface {
    private final MessagingService messagingService;
    private final LoggerPort loggerPort;
    private final Validator validator;

    @Autowired
    public ContactService(MessagingService messagingService, LoggerPort loggerPort, Validator validator) {
        this.messagingService = messagingService;
        this.loggerPort = loggerPort;
        this.validator = validator;
    }

    @Override
    public void sendContactFeedback(ContactDTO contactDTO) {
        Set<ConstraintViolation<ContactDTO>> violations = validator.validate(contactDTO);

        if (!violations.isEmpty()) {
            ConstraintViolation<ContactDTO> error = violations.stream().findFirst().get();
            throw new InvalidValueObjectException(error.getPropertyPath().toString(), error.getInvalidValue(), error.getMessage());
        }

        messagingService.sendFeedback(contactDTO.subject(), contactDTO.name(), contactDTO.email(), contactDTO.message());

        loggerPort.info(String.format("Contact feedback sent - Subject: %s, Name: %s, Email: %s, Message: %s", 
            contactDTO.subject(), contactDTO.name(), contactDTO.email(), contactDTO.message()));
    }
}
