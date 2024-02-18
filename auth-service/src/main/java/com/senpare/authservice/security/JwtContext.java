package com.senpare.authservice.security;

public class JwtContext {

    private static final ThreadLocal<String> CURRENT_JWT_TOKEN = new ThreadLocal<>();

    public static String getCurrentJwtToken() {
        return CURRENT_JWT_TOKEN.get();
    }

    public static void setCurrentJwtToken(String token) {
        CURRENT_JWT_TOKEN.set(token);
    }

    public static void clear() {
        CURRENT_JWT_TOKEN.remove();
    }
}
