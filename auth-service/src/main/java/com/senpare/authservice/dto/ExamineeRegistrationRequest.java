package com.senpare.authservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.UUID;

@JsonIgnoreProperties({ "password"})
@Getter
public class ExamineeRegistrationRequest extends AppUserRequest {

    private String examUuid;

    @Override
    public ExamineeRegistrationRequest setFirstName(String firstName) {
        return (ExamineeRegistrationRequest) super.setFirstName(firstName);
    }

    @Override
    public ExamineeRegistrationRequest setLastName(String lastName) {
        return (ExamineeRegistrationRequest) super.setLastName(lastName);
    }

    @Override
    public ExamineeRegistrationRequest setEmail(String email) {
        return (ExamineeRegistrationRequest) super.setEmail(email);
    }

    public ExamineeRegistrationRequest setExamUuid(String examUuid) {
        this.examUuid = examUuid;
        return this;
    }

    public ExamineeRegistrationRequest setExamUuid(UUID examUuid) {
        this.examUuid = examUuid.toString();
        return this;
    }
}
