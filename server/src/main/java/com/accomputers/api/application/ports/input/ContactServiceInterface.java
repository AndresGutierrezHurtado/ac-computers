package com.accomputers.api.application.ports.input;

import org.springframework.validation.annotation.Validated;

import com.accomputers.api.application.dtos.ContactDTO;
import jakarta.validation.Valid;

@Validated
public interface ContactServiceInterface {
    void sendContactFeedback(@Valid ContactDTO contactDTO);
}
