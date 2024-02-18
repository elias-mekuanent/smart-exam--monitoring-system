package com.senpare.examservice.controller;

import com.senpare.examservice.dto.ExamSectionDTO;
import com.senpare.examservice.dto.ExamSectionRequest;
import com.senpare.examservice.model.ExamSection;
import com.senpare.examservice.service.ExamSectionService;
import com.senpare.examservice.utilities.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/exam-service/exam-sections")
@Tag(name = "ExamSection endpoint", description = "An endpoint to manage exam sections.")
@RequiredArgsConstructor
public class ExamSectionController {

    private final ExamSectionService examSectionService;

    @PostMapping
    @Operation(summary = "Creates a new exam section.",
            responses = @ApiResponse(description = "ExamSection successfully created", responseCode = "204",
                    headers = @Header(name = "location", description = "Full path to the newly created exam section")))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<ExamSectionDTO> create(@RequestParam("examUuid") String examUuid, @RequestBody ExamSectionRequest examSectionRequest, HttpServletRequest request) {
        ExamSection createdExamSection = examSectionService.create(UUID.fromString(examUuid), examSectionRequest);
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .path("/" + createdExamSection.getUuid())
                .build()
                .toUri();

        return ResponseEntity.created(location).body(Util.toExamSectionDTO(createdExamSection));
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Returns an exam sections associated with the specified UUID.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<ExamSectionDTO> get(@PathVariable("uuid") String uuid) {
        ExamSection examSection = examSectionService.get(UUID.fromString(uuid));
        return ResponseEntity.ok(Util.toExamSectionDTO(examSection));
    }

    @GetMapping("/{uuid}/examinee")
    @Operation(summary = "Returns an exam section associated with the specified UUID. This endpoint is used by examinees while the exam is active (ExamStatus.STARTED).",
            responses = @ApiResponse(description = "ExamSection successfully retrieved", responseCode = "200"))
    @PreAuthorize("hasAnyRole('EXAMINEE')")
    public ResponseEntity<ExamSectionDTO> getForExaminee(@PathVariable("uuid") String uuid) {
        ExamSection examSection = examSectionService.getForExaminee(UUID.fromString(uuid));
        return ResponseEntity.ok(Util.toExamSectionDTO(examSection));
    }

    @GetMapping
    @Operation(summary = "Returns a paginated list of all exam section in the specified exam.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<ExamSectionDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size,
                                                @RequestParam(value = "sort", defaultValue = "createdOn") String sortBy,
                                                @RequestParam("examUuid") String examUuid) {
        Page<ExamSection> examSectionPage = examSectionService.getAll(page, size, sortBy, UUID.fromString(examUuid));
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(examSectionPage.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(examSectionPage.getTotalPages()));

        return new ResponseEntity<>(Util.toExamSectionDTOs(examSectionPage.getContent()), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/examinee")
    @Operation(summary = "Returns a paginated list of all exam section in the specified exam. This endpoint is used by examinees while the exam is active (ExamStatus.STARTED).")
    @PreAuthorize("hasAnyRole('EXAMINEE')")
    public ResponseEntity<List<ExamSectionDTO>> getAllForExaminee(@RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "size", defaultValue = "10") int size,
                                                       @RequestParam(value = "sort", defaultValue = "createdOn") String sortBy,
                                                       @RequestParam("examUuid") String examUuid) {
        Page<ExamSection> examSectionPage = examSectionService.getAllForExaminee(page, size, sortBy, UUID.fromString(examUuid));
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(examSectionPage.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(examSectionPage.getTotalPages()));

        return new ResponseEntity<>(Util.toExamSectionDTOs(examSectionPage.getContent()), responseHeaders, HttpStatus.OK);
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Updates an existing exam section.",
            responses = @ApiResponse(description = "ExamSection successfully updated", responseCode = "200"))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<ExamSectionDTO> update(@PathVariable("uuid") String uuid, @RequestBody ExamSectionRequest examSectionRequest) {
        ExamSection updatedExamSection = examSectionService.update(UUID.fromString(uuid), examSectionRequest);
        return ResponseEntity.ok(Util.toExamSectionDTO(updatedExamSection));
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete exam section associated with the specified UUID.",
            responses = @ApiResponse(description = "ExamSection successfully deleted", responseCode = "200"))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<Void> delete(@PathVariable("uuid") String uuid) {
        examSectionService.delete(UUID.fromString(uuid));
        return ResponseEntity.noContent().build();
    }
}
