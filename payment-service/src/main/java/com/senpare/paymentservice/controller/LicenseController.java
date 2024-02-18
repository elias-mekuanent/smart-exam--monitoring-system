package com.senpare.paymentservice.controller;

import com.senpare.paymentservice.dto.LicenseStatus;
import com.senpare.paymentservice.model.License;
import com.senpare.paymentservice.service.LicenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment-service/licenses")
@Tag(name = "License endpoint", description = "An endpoint to manage licenses.")
@RequiredArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;

    @GetMapping("/{uuid}")
    @Operation(summary = "Returns a license associated with the specified UUID.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<License> get(@PathVariable("uuid") String uuid) {
        License license = licenseService.get(UUID.fromString(uuid));
        return ResponseEntity.ok(license);
    }

    @GetMapping
    @Operation(summary = "Returns a paginated list of all licenses.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<License>> getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size,
                                                @RequestParam(value = "sort", defaultValue = "createdOn") String sortBy) {
        Page<License> licenses = licenseService.getAll(page, size, sortBy);
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(licenses.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(licenses.getTotalPages()));

        return new ResponseEntity<>(licenses.getContent(), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/examiner")
    @Operation(summary = "Returns a paginated list of all licenses that belong to the current examiner/user.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<List<License>> getAllByEmail(Principal principal,
                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "size", defaultValue = "10") int size,
                                                       @RequestParam(value = "sort", defaultValue = "createdOn") String sortBy,
                                                       @RequestParam(value = "licenseStatus", required = false) String licenseStatus) {
        Page<License> licenses = licenseService.getAllByEmail(principal.getName(), licenseStatus, page, size, sortBy);

        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(licenses.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(licenses.getTotalPages()));

        return new ResponseEntity<>(licenses.getContent(), responseHeaders, HttpStatus.OK);
    }

    @PutMapping("/use/{uuid}")
    @Operation(summary = "Updates the status of the license associated with the specified UUID to USED.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<License> use(Principal principal, @PathVariable("uuid") String uuid) {
        License license = licenseService.useLicense(UUID.fromString(uuid));
        return ResponseEntity.ok(license);
    }

    @GetMapping("/validate/{licenseKey}")
    @Operation(summary = "Returns true if the specified license key exists and not used, false otherwise.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<Boolean> validate(@PathVariable("licenseKey") String licenseKey) {
        boolean isValid = licenseService.isLicenseKeyValid(UUID.fromString(licenseKey));
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/validate-by-uuid/{uuid}")
    @Operation(summary = "Returns true if the specified license key exists and not used, false otherwise.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<Boolean> validateByUuid(@PathVariable("uuid") String uuid) {
        boolean isValid = licenseService.isLicenseValid(UUID.fromString(uuid));
        return ResponseEntity.ok(isValid);
    }
}
