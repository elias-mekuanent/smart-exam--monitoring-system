package com.senpare.examservice.controller;

import com.senpare.examservice.dto.ExamReport;
import com.senpare.examservice.service.ExamReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exam-service/exam-report")
@Tag(name = "ExamReport endpoint", description = "An endpoint to generate and manage exam exam report.")
@RequiredArgsConstructor
public class ExamReportController {

    private final ExamReportService examReportService;

    @GetMapping("/{examUuid}/current-examinee")
    @Operation(summary = "Get exam report of an examinee for the specified exam.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<ExamReport> getExamineeReport(@PathVariable("examUuid") String examUuid, @RequestParam("examineUuid") String examineeUuid) {
        ExamReport examReport = examReportService.getExamineeExamReport(UUID.fromString(examUuid), UUID.fromString(examineeUuid));

        return ResponseEntity.ok(examReport);
    }

    @GetMapping("/{examUuid}")
    @Operation(summary = "Get exam report of all examinees enrolled to an exam for the specified exam.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<List<ExamReport>> getExamineesReport(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam (value =  "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "firstName") String sort,
            @PathVariable("examUuid") String examUuid) {

        List<ExamReport> examReports = examReportService.getExamineesExamReport(page, size, sort, UUID.fromString(examUuid));
        return ResponseEntity.ok(examReports);
    }
}
