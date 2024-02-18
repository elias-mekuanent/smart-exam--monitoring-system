package com.senpare.authservice.controller;

import com.senpare.authservice.dto.AppUserDTO;
import com.senpare.authservice.dto.ChangePasswordDTO;
import com.senpare.authservice.model.AppUser;
import com.senpare.authservice.model.AppUserImage;
import com.senpare.authservice.service.AppUserImageService;
import com.senpare.authservice.service.AppUserService;
import com.senpare.authservice.utilities.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth-service/profile")
@Tag(name = "Profile endpoint", description = "Manage user's profile")
public class ProfileController {

    private final AppUserService appUserService;
    private final AppUserImageService appUserImageService;

    @GetMapping
    @Operation(summary = "Get authenticated user's detail")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER', 'EXAMINEE')")
    public ResponseEntity<AppUserDTO> get(Principal principal) {
        String username = principal.getName();

        AppUser appUser = appUserService.getAppUserByEmail(username);
        return ResponseEntity.ok(Util.toAppUserDTO(appUser));
    }

    @GetMapping(value = "/image", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @Operation(summary = "Get authenticated user's image. This is only supported for examinees at this time.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER', 'EXAMINEE')")
    public ResponseEntity<byte[]> getImage(Principal principal) {
        String username = principal.getName();

        AppUser appUser = appUserService.getAppUserByEmail(username);
        AppUserImage appUserImage = appUserImageService.getByAppUser(appUser);

        return ResponseEntity.ok(appUserImage.getImage());
    }

//    @PutMapping
//    public ResponseEntity<Void> update(Principal principal, @RequestBody UpdateAppUserRequest updateAppUserRequest) {
//        String username = principal.getName();
//
//        appUserService.updateByEmail(username, updateAppUserRequest);
//        return ResponseEntity.noContent().build();
//    }


    @PutMapping("/change-password")
    @Operation(summary = "Change the password of logged in user")
    public ResponseEntity<Void> changePassword(Principal principal, @RequestBody final ChangePasswordDTO changePasswordDTO) {
        String username = principal.getName();

        appUserService.changePassword(username, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.setAllow(Set.of(HttpMethod.PUT));
        return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
    }

}
