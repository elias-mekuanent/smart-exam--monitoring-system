package com.senpare.examservice.controller;

import com.senpare.examservice.dto.ExamDTO;
import com.senpare.examservice.dto.ExamDetail;
import com.senpare.examservice.dto.ExamRequest;
import com.senpare.examservice.dto.ExamSettingRequest;
import com.senpare.examservice.model.Exam;
import com.senpare.examservice.model.enumeration.ExamStatus;
import com.senpare.examservice.service.ExamEnrollmentService;
import com.senpare.examservice.service.ExamService;
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
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exam-service/exams")
@Tag(name = "Exam endpoint", description = "An endpoint to manage exams.")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private final ExamEnrollmentService examEnrollmentService;

    @PostMapping
    @Operation(summary = "Creates a new exam.",
            responses = @ApiResponse(description = "Exam successfully created", responseCode = "204",
                    headers = @Header(name = "location", description = "Full path to the newly created exam")))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<ExamDTO> create(@RequestBody ExamRequest examRequest, HttpServletRequest request) {
        Exam createdExam = examService.create(examRequest);
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .path("/" + createdExam.getUuid())
                .build()
                .toUri();

        return ResponseEntity.created(location).body(Util.toExamDTO(createdExam));
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Returns an exam associated with the specified UUID.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<ExamDTO> get(@PathVariable("uuid") String uuid) {
        Exam exam = examService.get(UUID.fromString(uuid));
        return ResponseEntity.ok(Util.toExamDTO(exam));
    }

    @GetMapping("{uuid}/detail")
    @Operation(summary = "Returns an exam associated with the specified UUID. This will be used to show details of an exam to examinee before starting the exam.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER', 'EXAMINEE')")
    public ResponseEntity<ExamDetail> getPlannedExamStartingDateTime(@PathVariable("uuid") String examUuid) {
        Exam exam = examService.get(UUID.fromString(examUuid));
        return ResponseEntity.ok(Util.toExamDetail(exam));
    }


    @GetMapping
    @Operation(summary = "Returns a paginated list of all exams.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<ExamDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size,
                                                @RequestParam(value = "sort", defaultValue = "createdOn") String sortBy) {
        Page<Exam> examPage = examService.getAll(page, size, sortBy);
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(examPage.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(examPage.getTotalPages()));

        return new ResponseEntity<>(Util.toExamDTOs(examPage.getContent()), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/examiner")
    @Operation(summary = "Returns a paginated list of all exams created by examiner. This endpoint is only available for examiners.")
    @PreAuthorize("hasAnyRole('EXAMINER')")
    public ResponseEntity<List<ExamDTO>> getAllExamsForExaminer(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size,
                                                                @RequestParam(value = "sort", defaultValue = "createdOn") String sortBy,
                                                                Principal principal) {
        Page<Exam> examPage = examService.getAllExamsForExaminer(principal.getName(), page, size, sortBy);
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(examPage.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(examPage.getTotalPages()));

        return new ResponseEntity<>(Util.toExamDTOs(examPage.getContent()), responseHeaders, HttpStatus.OK);
    }


    @GetMapping("/count-by-status")
    @Operation(summary = "Returns number Of exams by status. Exam status can be one of the following: CREATED, STARTED, COMPLETED, CANCELED. This endpoint is only available for examiners.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<Integer> getNumberOfExamsByStatus(@RequestParam("examStatus") String status) {
        int numberOfExams = examService.countByStatus(ExamStatus.lookupByCode(status));
        return ResponseEntity.ok(numberOfExams);
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Update exam details. Exam details can't be updated once exam is started.",
            responses = @ApiResponse(description = "Exam successfully updated", responseCode = "200"))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<ExamDTO> update(@PathVariable("uuid") String uuid, @RequestBody ExamRequest examRequest) {
        Exam exam = examService.update(UUID.fromString(uuid), examRequest);
        return ResponseEntity.ok(Util.toExamDTO(exam));
    }

    @PutMapping("/{uuid}/update-status")
    @Operation(summary = "Update exam status. Exam statuses include one of the following: STARTED, COMPLETED, CANCELLED.",
            responses = @ApiResponse(description = "Exam status successfully updated", responseCode = "200"))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<ExamDTO> updateStatus(@PathVariable("uuid") String uuid, @RequestParam("examStatus") String examStatus) {
        Exam exam = examService.updateStatus(UUID.fromString(uuid), ExamStatus.lookupByCode(examStatus));
        return ResponseEntity.ok(Util.toExamDTO(exam));
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete exam associated with the specified UUID. Exam can't be deleted during an exam.",
            responses = @ApiResponse(description = "Exam successfully deleted", responseCode = "200"))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<Void> delete(@PathVariable("uuid") String uuid) {
        examService.delete(UUID.fromString(uuid));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}/update-setting")
    @Operation(summary = "Update exam setting. Exam setting can't be updated once exam is started.",
            responses = @ApiResponse(description = "Exam setting successfully updated", responseCode = "200"))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<ExamDTO> updateExamSetting(@PathVariable("uuid") String uuid, @RequestBody ExamSettingRequest examSettingRequest) {
        Exam exam = examService.updateExamSetting(UUID.fromString(uuid), examSettingRequest);
        return ResponseEntity.ok(Util.toExamDTO(exam));
    }


    @PostMapping("/{examUuid}/enroll")
    @Operation(summary = "Enroll examinee to an exam. Examinee can only enroll to an active exam.",
            responses = @ApiResponse(description = "Examinee successfully enrolled", responseCode = "200"))
    @PreAuthorize("hasAnyRole('EXAMINEE')")
    public ResponseEntity<ExamDTO> enroll(@PathVariable("examUuid") String examUuid, Principal principal) {
        Exam exam = examEnrollmentService.create(UUID.fromString(examUuid), principal.getName());
        return ResponseEntity.ok(Util.toExamDTO(exam));
    }

    @PostMapping("/{examUuid}/complete-exam-for-examinee")
    @Operation(summary = "Complete exam to an examinee. Examinee can only complete an exam that he/she attended.",
            responses = @ApiResponse(description = "Exam successfully completed for the current examinee", responseCode = "200"))
    @PreAuthorize("hasAnyRole('EXAMINEE')")
    public ResponseEntity<Void> completeExam(@PathVariable("examUuid") String examUuid, Principal principal) {
        examEnrollmentService.completedExamForExaminee(UUID.fromString(examUuid), principal.getName());
        return ResponseEntity.ok().build();
    }

}
