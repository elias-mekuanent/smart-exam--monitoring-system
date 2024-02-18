package com.senpare.authservice.controller;

import com.senpare.authservice.dto.AppUserDTO;
import com.senpare.authservice.dto.ChangePasswordDTO;
import com.senpare.authservice.dto.AppUserRequest;
import com.senpare.authservice.dto.UpdateAppUserRequest;
import com.senpare.authservice.model.AppUser;
import com.senpare.authservice.service.AppUserRoleService;
import com.senpare.authservice.service.AppUserService;
import com.senpare.authservice.utilities.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth-service/users")
@Tag(name = "User endpoint", description = "CRUD operation on user")
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserRoleService appUserRoleService;

    @PostMapping
    @Operation(summary = "Create user")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<AppUserDTO> createAppUser(@RequestBody @Valid AppUserRequest appUserRequest, HttpServletRequest request) {
        AppUser createdAppUser = appUserService.create(appUserRequest, true);
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .path("/" + createdAppUser.getUuid())
                .build()
                .toUri();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setAllow(Set.of(HttpMethod.GET, HttpMethod.POST));
        responseHeaders.setLocation(location);

        return new ResponseEntity<>(Util.toAppUserDTO(createdAppUser), responseHeaders, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get a list of users")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<AppUserDTO>> getAppUsers(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam (value =  "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "firstName") String sort) {
        Page<AppUser> appUsers = appUserService.getAppUsers(page, size, sort);
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(appUsers.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(appUsers.getTotalPages()));

        responseHeaders.setAllow(Set.of(HttpMethod.GET, HttpMethod.POST));
        return new ResponseEntity<>(Util.toAppUserDTOS(appUsers.getContent()), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/examinees")
    @Operation(summary = "Get a list of examinees enrolled to an exam")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<List<AppUserDTO>> getExamineesByExamUuid(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam (value =  "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "firstName") String sort,
            @RequestParam(value = "examUuid") String examUuid) {
        Page<AppUser> appUsers = appUserService.getAppUsersByExamUuid(page, size, sort, examUuid);
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(appUsers.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(appUsers.getTotalPages()));

        responseHeaders.setAllow(Set.of(HttpMethod.GET, HttpMethod.POST));
        return new ResponseEntity<>(Util.toAppUserDTOS(appUsers.getContent()), responseHeaders, HttpStatus.OK);
    }


    @GetMapping("{uuid}")
    @Operation(summary = "Get user by uuid")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<AppUserDTO> getAppUser(@PathVariable("uuid") final String uuid) {
        AppUser appUser = appUserService.getAppUser(UUID.fromString(uuid));
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.setAllow(Set.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE));
        return new ResponseEntity<>(Util.toAppUserDTO(appUser), responseHeaders, HttpStatus.OK);
    }

    @PutMapping("{uuid}")
    @Operation(summary = "Update user details")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<AppUserDTO> updateAppUser(@PathVariable("uuid") final String uuid, @RequestBody final UpdateAppUserRequest request) {
        AppUser updatedAppUser = appUserService.update(UUID.fromString(uuid), request);
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.setAllow(Set.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE));
        return new ResponseEntity<>(Util.toAppUserDTO(updatedAppUser), responseHeaders, HttpStatus.OK);
    }

    @DeleteMapping("{uuid}")
    @Operation(summary = "Delete user")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> deleteAppUser(@PathVariable("uuid") final String uuid) {
        appUserService.delete(UUID.fromString(uuid));
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.setAllow(Set.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE));
        return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{uuid}/reset-password")
    @Operation(summary = "Reset user's password")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> resetPassword(@PathVariable("uuid") final String uuid, @RequestBody final ChangePasswordDTO changePasswordDTO) {
        appUserService.changePassword(UUID.fromString(uuid), changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.setAllow(Set.of(HttpMethod.PUT));
        return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{uuid}/assign-role")
    @Operation(summary = "Assign privileges to the specified role")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> assignRole(@PathVariable("uuid") final UUID uuid, @RequestBody final List<String> roleUuids) {
        appUserRoleService.assignRoleByRoleUuids(uuid, roleUuids);
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.setAllow(Set.of(HttpMethod.PUT));
        return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
    }
    @GetMapping("/count-examiner")
    @Operation(summary = "Get the total number of examiners")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Integer> getTotalNumberOfExaminers() {
        int totalNumberOfExaminers = appUserService.getTotalNumberOfExaminers();
        return ResponseEntity.ok(totalNumberOfExaminers);
    }
}
