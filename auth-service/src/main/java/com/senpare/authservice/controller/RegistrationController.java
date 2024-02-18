package com.senpare.authservice.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.senpare.authservice.dto.AppUserDTO;
import com.senpare.authservice.dto.ExamineeRegistrationRequest;
import com.senpare.authservice.dto.ExaminerRegistrationRequest;
import com.senpare.authservice.model.AppUser;
import com.senpare.authservice.service.RegistrationService;
import com.senpare.authservice.utilities.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth-service")
@RestController
@Tag(name = "Registration endpoint", description = "Users registration and verification endpoint")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register/examiner")
    @Operation(summary = "Creates a new examiner account")
    public ResponseEntity<AppUserDTO> registerExaminer(@RequestBody final ExaminerRegistrationRequest examinerRegistrationRequest) {
        AppUser registeredAppUser = registrationService.registerExaminer(examinerRegistrationRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setAllow(Set.of(HttpMethod.POST));

        return new ResponseEntity<>(Util.toAppUserDTO(registeredAppUser), headers,  HttpStatus.CREATED);
    }

    @PostMapping(value = "/register/examinee", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Creates a new examinee account")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<AppUserDTO> registerExaminee(@RequestParam("model") final String request,
                                                       @RequestPart("file") MultipartFile multipartFile) throws IOException {
        ExamineeRegistrationRequest examineeRegistrationRequest = new JsonMapper().readValue(request, ExamineeRegistrationRequest.class);
        AppUser registeredAppUser = registrationService.registerExaminee(examineeRegistrationRequest, multipartFile.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setAllow(Set.of(HttpMethod.POST));

        return new ResponseEntity<>(Util.toAppUserDTO(registeredAppUser), headers,  HttpStatus.CREATED);
    }

    @PostMapping(value = "/bulk-import/examinee", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Bulk import exainees")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<Void> importExaminees(@RequestParam("examinUuid") String examUuid, @RequestPart("file") MultipartFile multipartFile) throws IOException {
        registrationService.bulkImport(multipartFile, UUID.fromString(examUuid));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<Void> confirmEmail(@RequestParam("token") final String token) {
        registrationService.confirmToken(token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/resend-confirmation-email")
    public ResponseEntity<Void> resendConfirmationEmail(@RequestParam("appUserUuid") final String appUserUuid) {
        registrationService.resendToken(UUID.fromString(appUserUuid));
        return ResponseEntity.ok().build();
    }
}
