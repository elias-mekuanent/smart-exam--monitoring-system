package com.senpare.authservice.dto;

import lombok.Getter;

import java.util.Set;

@Getter
public class AppUserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public AppUserRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AppUserRequest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AppUserRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public AppUserRequest setPassword(String password) {
        this.password = password;
        return this;
    }

}
