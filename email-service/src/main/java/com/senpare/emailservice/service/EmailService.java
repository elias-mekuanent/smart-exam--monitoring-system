package com.senpare.emailservice.service;

import java.nio.charset.StandardCharsets;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.senpare.emailservice.dto.EmailDTO;

@Service
public class EmailService implements EmailSender {
    private final static Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender javaMailSender;
    private final String emailFrom;

    // For some reason lombok is not working here
    public EmailService(JavaMailSender javaMailSender, @Value("${app.mail.from}") String emailFrom) {
        this.javaMailSender = javaMailSender;
        this.emailFrom = emailFrom;
    }

    @Override
    @Async
    public void send(EmailDTO emailDTO) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, String.valueOf(StandardCharsets.UTF_8));
            helper.setText(emailDTO.getBody(), true);
            helper.setTo(emailDTO.getTo());
            helper.setSubject(emailDTO.getSubject());
            helper.setFrom(emailFrom);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send body", e);
            // TODO: handle this exception
            throw new IllegalStateException("Failed to send body: " + e.getMessage());
        }

    }
}
