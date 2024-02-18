package com.senpare.apigateway.security;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final Map<String, List<HttpMethod>> openApiEndpoints = Map.of(
            "/api/v1/auth-service/token", List.of(HttpMethod.POST),
            "/api/v1/auth-service/token/refresh", List.of(HttpMethod.POST),
            "/api/v1/auth-service/token/validate", List.of(HttpMethod.GET),
            "/api/v1/payment-service/license-types", List.of(HttpMethod.GET),
            "/api/v1/auth-service/register/examiner", List.of(HttpMethod.POST),
            "/api/v1/auth-service/confirm-email", List.of(HttpMethod.GET),
            "/api/v1/auth-service/resend-confirmation-email", List.of(HttpMethod.PUT),
            "/api/v1/audio-processing-service/measure-noise", List.of(HttpMethod.POST),
            "/v3/api-docs", List.of(HttpMethod.POST, HttpMethod.GET, HttpMethod.DELETE, HttpMethod.PUT)
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints.entrySet()
                    .stream() // TODO: remove the first check
                    .noneMatch(entry -> request.getURI().getPath().startsWith("/api/v1/payment-service/payments/chapa/verify") || (request.getURI().getPath().contains(entry.getKey()) && entry.getValue().contains(request.getMethod())));
}