package com.accomputers.api.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Ports
import com.accomputers.api.application.ports.output.MessagingService;

@Service
public class EmailService implements MessagingService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendFeedback(String subject, String name, String email, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("andres52885241@gmail.com");
        mail.setSubject("[AC Computers] Feedback form: " + subject);
        mail.setText(
                String.format(
                        "You have received a new feedback from:\n\n" +
                                "%nName: %s\n" +
                                "%nEmail: %s\n" +
                                "%nMessage: %s",
                        name,
                        email,
                        message));
        mailSender.send(mail);
    }
}
