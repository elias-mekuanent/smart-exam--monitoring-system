package com.senpare.authservice.service;

import com.senpare.authservice.exception.BadRequestException;
import com.senpare.authservice.exception.ResourceNotFoundException;
import com.senpare.authservice.model.AppUser;
import com.senpare.authservice.model.AppUserImage;
import com.senpare.authservice.repository.AppUserImageRepository;
import com.senpare.authservice.utilities.AuthServiceMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AppUserImageService {

    private final AppUserImageRepository appUserImageRepository;

    public AppUserImage create(AppUser appUser, byte[] image) {
        if(appUser == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("AppUser"));
        }

        AppUserImage appUserImage = new AppUserImage()
                .setUuid(UUID.randomUUID())
                .setAppUser(appUser)
                .setImage(image);

        return appUserImageRepository.save(appUserImage);
    }

    public AppUserImage getByAppUser(AppUser appUser) {
        if(appUser == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("AppUser"));
        }

        return appUserImageRepository.findByAppUser(appUser)
                .orElseThrow(() -> new ResourceNotFoundException(AuthServiceMessages.canNotBeFound("AppUserImage", "appUser", appUser.getEmail())));
    }
}
