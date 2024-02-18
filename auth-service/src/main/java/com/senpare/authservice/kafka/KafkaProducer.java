package com.senpare.authservice.kafka;

import com.senpare.authservice.dto.EmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, EmailDTO> kafkaTemplate;


    @Async
    public void pushEmailToKafka(EmailDTO emailDTO) {
        CompletableFuture<SendResult<String, EmailDTO>> registrationEmail = kafkaTemplate.send("registrationEmail", emailDTO);
//        registrationEmail.addCallback(success -> log.info("Email was successfully pushed to kafka"), failure -> log.info("Email was unable to be pushed to kafka"));
    }
}
