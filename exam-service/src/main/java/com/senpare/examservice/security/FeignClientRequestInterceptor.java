package com.senpare.examservice.security;

import com.senpare.examservice.utilities.Util;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignClientRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String token = JwtContext.getCurrentJwtToken();
        if(Util.isNotNullAndEmpty(token)) {
            template.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            log.info("JWT token relayed to " + template.url());
        } else {
            log.warn("No JWT token found in the request context");
        }
    }
}
