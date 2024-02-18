package com.senpare.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ConfigurationBeans {

    @Bean
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public RequestMatcher allowedRequestPath() {
        final String BASE_URL = "/api/v1/auth-service";
        List<String> allowedPaths = List.of(
                BASE_URL + "/register/examiner/**",
                BASE_URL + "/confirm-email/**",
                BASE_URL + "/resend-confirmation-email/**",
                BASE_URL + "/token/**",
                BASE_URL + "/logout",
                "/webjars/**",
                "/css/**",
                "/js/**",
                "/auth-service/swagger-ui.html",
                "/auth-service/swagger-ui/**",
                "/auth-service/v3/api-docs/**"
        );

        List<RequestMatcher> requestMatchers = new ArrayList<>();
        for(String path : allowedPaths) {
            requestMatchers.add(new AntPathRequestMatcher(path));
        }

        RequestMatcher chainedRequestMatchers = new OrRequestMatcher(requestMatchers);
        return chainedRequestMatchers;
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedMethods("*")
//                        .allowedOrigins("*"); // TODO: this should be changed to the frontend url
//            }
//        };
//    }
}
