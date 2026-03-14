package com.accomputers.api.application.ports.output;

public interface MessagingService {
    void sendFeedback(String subject, String name, String email, String message);
    void sendPasswordSetup(String name, String email, String setupLink);
    void sendPasswordReset(String name, String email, String resetLink);
    void sendPasswordChangedNotification(String name, String email, String contactPageUrl);
}
