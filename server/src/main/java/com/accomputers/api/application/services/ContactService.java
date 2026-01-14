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
    private static final String CONTACT_EMAIL = "andres52885241@gmail.com";
    
    private final MessagingService messagingService;
    private final LoggerPort loggerPort;

    @Autowired
    public ContactService(MessagingService messagingService, LoggerPort loggerPort) {
        this.messagingService = messagingService;
        this.loggerPort = loggerPort;
    }

    @Override
    public void sendContactFeedback(ContactDTO contactDTO) {
        String subject = String.format("Nuevo mensaje de contacto de %s", contactDTO.name());
        
        String emailBody = String.format(
            "Has recibido un nuevo mensaje de contacto desde el formulario de contacto de AC Computers:\n\n" +
            "Nombre: %s\n" +
            "Email: %s\n" +
            "Mensaje:\n%s",
            contactDTO.name(),
            contactDTO.email(),
            contactDTO.message()
        );

        messagingService.sendEmail(CONTACT_EMAIL, subject, emailBody);

        loggerPort.info(String.format("Contact feedback sent - Name: %s, Email: %s", 
            contactDTO.name(), contactDTO.email()));
    }
}
