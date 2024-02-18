package com.senpare.emailservice.service;

import com.senpare.emailservice.dto.EmailDTO;

public interface EmailSender {

    void send(EmailDTO emailDTO);
}
