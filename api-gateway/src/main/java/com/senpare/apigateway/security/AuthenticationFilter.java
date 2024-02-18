package com.senpare.apigateway.security;

import com.senpare.apigateway.exception.BadAuthorizationRequestException;
import com.senpare.apigateway.security.RouteValidator;
import com.senpare.apigateway.utilities.JwtService;
import com.senpare.apigateway.utilities.Util;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter {

    private final RouteValidator routeValidator;
    private final JwtService jwtService;

    public AuthenticationFilter(RouteValidator routeValidator, JwtService jwtService) {
        this.routeValidator = routeValidator;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if(!routeValidator.isSecured.test(request)) {
            return chain.filter(exchange);
        }

        if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            throw new BadAuthorizationRequestException("Authorization header not found in the request");
        }

        String authHeader = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
        if(Util.isNullOrEmpty(authHeader) || !authHeader.startsWith("Bearer ") || authHeader.length() <= 7) {
            throw new BadAuthorizationRequestException("Malformed authorization header");
        }

        String token = authHeader.substring(7);
        if(jwtService.isInvalid(token)) {
            throw new BadAuthorizationRequestException("Invalid or expired access token");
        }

        return chain.filter(exchange.mutate()
                        .request(exchange.getRequest()
                                .mutate()
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .build())
                        .build());

    }
}