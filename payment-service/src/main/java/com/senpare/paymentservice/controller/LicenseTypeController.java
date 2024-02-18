package com.senpare.paymentservice.controller;

import com.senpare.paymentservice.dto.LicenseTypeRequest;
import com.senpare.paymentservice.model.LicenseType;
import com.senpare.paymentservice.service.LicenseTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment-service/license-types")
@Tag(name = "LicenseType endpoint", description = "An endpoint to manage license types.")
@Slf4j
@RequiredArgsConstructor
public class LicenseTypeController {

    private final LicenseTypeService licenseTypeService;

    @PostMapping
    @Operation(summary = "Creates a new license type.",
            responses = @ApiResponse(description = "License type successfully created", responseCode = "204",
                    headers = @Header(name = "location", description = "Full path to the newly created license type")))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<LicenseType> create(@Valid @RequestBody LicenseTypeRequest licenseTypeRequest, HttpServletRequest request) {
        LicenseType licenseType = licenseTypeService.create(licenseTypeRequest);
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .path("/" + licenseType.getUuid())
                .build()
                .toUri();

        return ResponseEntity.created(location).body(licenseType);
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Returns a license type associated with the specified UUID.")
    public ResponseEntity<LicenseType> get(@PathVariable("uuid") String uuid) {
        LicenseType licenseType = licenseTypeService.get(UUID.fromString(uuid));
        return ResponseEntity.ok(licenseType);
    }

    @GetMapping
    @Operation(summary = "Returns a paginated list of all license types.")
    public ResponseEntity<List<LicenseType>> getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                                    @RequestParam(value = "sort", defaultValue = "typeName") String sortBy) {
        Page<LicenseType> licenseTypes = licenseTypeService.getAll(page, size, sortBy);
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(licenseTypes.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(licenseTypes.getTotalPages()));

        return new ResponseEntity<>(licenseTypes.getContent(), responseHeaders, HttpStatus.OK);
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Updates a license type associated with the specified UUID by the given payload. Empty or null fields will be ignored.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<LicenseType> update(@PathVariable("uuid") String uuid, @RequestBody LicenseTypeRequest licenseTypeRequest) {
        LicenseType licenseType = licenseTypeService.update(UUID.fromString(uuid), licenseTypeRequest);
        return ResponseEntity.ok(licenseType);
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Deletes a license type associated with the specified UUID. A license type that is associated with any license can't be deleted.",
            responses = @ApiResponse(description = "License type successfully documented", responseCode = "204"))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<LicenseType> delete(@PathVariable("uuid") String uuid) {
        licenseTypeService.delete(UUID.fromString(uuid));
        return ResponseEntity.noContent().build();
    }

}
