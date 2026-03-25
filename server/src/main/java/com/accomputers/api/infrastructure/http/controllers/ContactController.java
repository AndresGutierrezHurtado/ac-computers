package com.accomputers.api.infrastructure.http.controllers;

// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

// Application
import com.accomputers.api.application.dtos.ContactDTO;
import com.accomputers.api.application.ports.input.ContactServiceInterface;

// Infrastructure
import com.accomputers.api.infrastructure.http.responses.ResponseDTO;

@RestController
@RequestMapping("/contact")
public class ContactController {
    private final ContactServiceInterface contactServiceInterface;

    @Autowired
    public ContactController(ContactServiceInterface contactServiceInterface) {
        this.contactServiceInterface = contactServiceInterface;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> sendContactFeedback(@RequestBody @Valid ContactDTO contactDTO) {
        contactServiceInterface.sendContactFeedback(contactDTO);

        ResponseDTO<Void> responseDTO = new ResponseDTO<>(
                "Feedback de contacto enviado exitosamente",
                true);

        return ResponseEntity.ok(responseDTO);
    }
}
