//package com.senpare.apigateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
//import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,  ReactiveClientRegistrationRepository clientRegistrationRepository) {
//         http
//            .authorizeExchange()
//            .anyExchange().authenticated()
//            .and()
//            .oauth2Login(Customizer.withDefaults());
//
//        http.logout(logout -> {
//            OidcClientInitiatedServerLogoutSuccessHandler handler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
////            handler.setPostLogoutRedirectUri("{baseUrl}");
//            // TODO: Invalidate the session
//            logout.logoutSuccessHandler(handler);
//        });
//
//        return http.build();
//    }
//}