package com.senpare.examservice.controller;

import com.senpare.examservice.model.enumeration.AttendanceStatus;
import com.senpare.examservice.model.enumeration.SecurityStrikeType;
import com.senpare.examservice.service.ExamSecurityStrikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exam-service/exam-security-strike")
@Tag(name = "ExamSecurityStrike endpoint", description = "An endpoint to manage exam security.")
@RequiredArgsConstructor
public class ExamSecurityStrikeController {

    private final ExamSecurityStrikeService examSecurityStrikeService;

    @PostMapping
    @Operation(summary = "Adds a new security strike to the exam for current examnee. " +
            "Security strikes can be one of the following: LEAVING_EXAM_PAGE, BACKGROUND_NOISE, MULTIPLE_PEOPLE_ON_SCREEN, NETWORK_ISSUES. " +
            "It returns the current attendance status of the current examinee as either SUSPENDED or PRESENT. If the returned status is SUSPENDED," +
            "the examinee will be logged out of the exam")
    @PreAuthorize("hasRole('EXAMINEE')")
    public ResponseEntity<AttendanceStatus> create(@RequestParam("examUuid") String examUuid, String strikeType, Principal principal) {
        AttendanceStatus attendanceStatus = examSecurityStrikeService.create(UUID.fromString(examUuid), SecurityStrikeType.lookupByCode(strikeType), principal.getName());
        return ResponseEntity.ok(attendanceStatus);
    }
}
