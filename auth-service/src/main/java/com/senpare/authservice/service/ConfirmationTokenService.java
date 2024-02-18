package com.senpare.authservice.service;

import com.senpare.authservice.exception.BadRequestException;

import com.senpare.authservice.exception.ResourceAlreadyExistsException;
import com.senpare.authservice.exception.ResourceNotFoundException;
import com.senpare.authservice.model.AppUser;
import com.senpare.authservice.model.ConfirmationToken;
import com.senpare.authservice.repository.ConfirmationTokenRepository;
import com.senpare.authservice.utilities.AuthServiceMessages;
import com.senpare.authservice.utilities.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final Clock clock;

    public ConfirmationToken create(final ConfirmationToken confirmationToken) {
        String token = confirmationToken.getToken();
        if(Util.isNullOrEmpty(token)) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("Confirmation.Token"));
        }

        if(confirmationToken.getExpiresAt() == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("ExpiredAt"));
        }

        if(confirmationToken.getConfirmedAt() != null) {
            throw new ResourceAlreadyExistsException(AuthServiceMessages.alreadyConfirmed("Token"));
        }

        if(isTokenExist(token)) {
            throw new ResourceAlreadyExistsException(AuthServiceMessages.alreadyExists("Token"));
        }

        if(confirmationToken.getUuid() == null) {
            confirmationToken.setUuid(UUID.randomUUID());
        }

        return confirmationTokenRepository.save(confirmationToken);
    }

    public ConfirmationToken getToken(String token) {
        if(Util.isNullOrEmpty(token)) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("ConfirmationToken.Token"));
        }
        return confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException(AuthServiceMessages.canNotBeFound("ConfirmationToken", "token", token));
                });
    }

    public ConfirmationToken getConfirmationTokenByAppUser(AppUser appUser) {
        if(appUser == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("AppUser"));
        }

        return confirmationTokenRepository.findByAppUser(appUser)
                .orElseThrow(() -> new BadRequestException(AuthServiceMessages.canNotBeFound("ConfirmationToken", "appUser")));
    }

    @Transactional
    public ConfirmationToken delete(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("ConfirmationToken.UUID"));
        }

        ConfirmationToken confirmationToken = confirmationTokenRepository.findById(uuid)
                .orElseThrow(() -> new BadRequestException(AuthServiceMessages.canNotBeFound("ConfirmationToken", "uuid", uuid.toString())));

        confirmationTokenRepository.deleteToken(confirmationToken);

        return confirmationToken;
    }

    public void setConfirmedAt(final String token) {
        if(!isTokenExist(token)) {
            throw new ResourceNotFoundException(AuthServiceMessages.canNotBeFound("ConfirmationToken", "token", token));
        }
        confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now(clock));
    }

    public boolean isTokenExist(final String token) {
        if(Util.isNullOrEmpty(token)) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNullOrEmpty("ConfirmationToken.Token"));
        }
        return confirmationTokenRepository
                .findByToken(token)
                .isPresent();
    }

}