package com.accomputers.api.application.ports.input;

import com.accomputers.api.application.dtos.ContactDTO;

public interface ContactServiceInterface {
    void sendContactFeedback(ContactDTO contactDTO);
}
