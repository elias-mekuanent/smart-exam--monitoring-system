package com.senpare.authservice.dto;

import lombok.Getter;

@Getter
public class EmailDTO {

    private String to;
    private String subject;
    private String body;

    public EmailDTO setTo(String to) {
        this.to = to;
        return this;
    }

    public EmailDTO setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailDTO setBody(String body) {
        this.body = body;
        return this;
    }
}
