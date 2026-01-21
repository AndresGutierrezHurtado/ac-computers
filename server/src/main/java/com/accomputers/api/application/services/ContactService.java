package com.accomputers.api.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Ports
import com.accomputers.api.application.ports.input.ContactServiceInterface;
import com.accomputers.api.application.ports.output.LoggerPort;
import com.accomputers.api.application.ports.output.MessagingService;

// DTOs
import com.accomputers.api.application.dtos.ContactDTO;

@Service
public class ContactService implements ContactServiceInterface {
    private final MessagingService messagingService;
    private final LoggerPort loggerPort;

    @Autowired
    public ContactService(MessagingService messagingService, LoggerPort loggerPort) {
        this.messagingService = messagingService;
        this.loggerPort = loggerPort;
    }

    @Override
    public void sendContactFeedback(ContactDTO contactDTO) {
        messagingService.sendFeedback(contactDTO.subject(), contactDTO.name(), contactDTO.email(), contactDTO.message());

        loggerPort.info(String.format("Contact feedback sent - Subject: %s, Name: %s, Email: %s, Message: %s", 
            contactDTO.subject(), contactDTO.name(), contactDTO.email(), contactDTO.message()));
    }
}
