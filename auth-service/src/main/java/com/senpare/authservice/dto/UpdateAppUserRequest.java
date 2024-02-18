package com.senpare.authservice.dto;

import lombok.Getter;

@Getter
public class UpdateAppUserRequest {

    private String firstName;
    private String lastName;
    private String email;

    public UpdateAppUserRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UpdateAppUserRequest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UpdateAppUserRequest setEmail(String email) {
        this.email = email;
        return this;
    }
}
