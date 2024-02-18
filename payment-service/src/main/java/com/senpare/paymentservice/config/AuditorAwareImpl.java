package com.senpare.paymentservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        log.info("Inside getCurrentAuditor() API");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return Optional.of("SYSTEM");
        }
        String user = authentication.getName();


        log.info("Logged in user information ::: " + user); // Not getting called during update operation

        return Optional.of(user);
    }

}