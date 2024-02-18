package com.senpare.authservice.dto;

import lombok.Getter;

@Getter
public class AuthRequest {

    private String username;
    private String password;

    public AuthRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public AuthRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
