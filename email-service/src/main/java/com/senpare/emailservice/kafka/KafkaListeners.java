package com.senpare.emailservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.senpare.emailservice.dto.EmailDTO;
import com.senpare.emailservice.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class KafkaListeners {

    private final EmailService emailService;
    private final JsonMapper jsonMapper = new JsonMapper();

    @KafkaListener(topics = "registrationEmail", groupId = "email")
    public void registrationEmailConsumer(String data)  throws Throwable {
        System.out.println("Data" + data);
        // TODO: validate data here
        EmailDTO emailDTO = jsonMapper.readValue(data, EmailDTO.class);
        emailService.send(emailDTO);
        log.info("Confirmation email sent");
    }
}
