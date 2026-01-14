package com.accomputers.api.application.ports.output;

public interface MessagingService {
    void sendEmail(String to, String subject, String text);
}
