package com.accomputers.api.infrastructure.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

// Ports
import com.accomputers.api.application.ports.output.MessagingService;

@Service
public class EmailService implements MessagingService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendFeedback(String subject, String name, String email, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo("andres52885241@gmail.com");
            helper.setSubject("[AC Computers] Feedback form: " + subject);
            helper.setText(EmailTemplates.buildHtmlEmail(name, email, message), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }

}
