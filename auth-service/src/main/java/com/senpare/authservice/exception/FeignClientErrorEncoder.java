package com.senpare.authservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class FeignClientErrorEncoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        FeignClientExceptionMessage feignClientExceptionMessage;
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            feignClientExceptionMessage = mapper.readValue(bodyIs, FeignClientExceptionMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }

        String message = feignClientExceptionMessage.getMessage();
        log.error(message);

        switch (response.status()) {
            case 400:
                return new BadRequestException(message);
            case 401:
                return new BadCredentialsException(message);
            case 404:
                return new ResourceNotFoundException(message);
            case 409:
                return new ResourceAlreadyExistsException(message);
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }

}