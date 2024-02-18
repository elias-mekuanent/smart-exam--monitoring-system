package com.senpare.examservice.service;

import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.model.Exam;
import com.senpare.examservice.model.ExamSecurityStrike;
import com.senpare.examservice.model.enumeration.AttendanceStatus;
import com.senpare.examservice.model.enumeration.ExamStatus;
import com.senpare.examservice.model.enumeration.SecurityStrikeType;
import com.senpare.examservice.repository.ExamSecurityStrikeRepository;
import com.senpare.examservice.utilities.ExamServiceMessages;
import com.senpare.examservice.utilities.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamSecurityStrikeService {

    private final ExamService examService;
    private final ExamSecurityStrikeRepository examSecurityStrikeRepository;
    private final ExamEnrollmentService examEnrollmentService;

    public AttendanceStatus create(UUID examUuid, SecurityStrikeType securityStrikeType, String examineeEmail) {
        if(securityStrikeType == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("SecurityStrikeType"));
        }

        if(Util.isNullOrEmpty(examineeEmail)) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("AppUser.Email"));
        }

        Exam exam = examService.get(examUuid);
        if(exam.getExamStatus() != ExamStatus.STARTED) {
            throw new BadRequestException(ExamServiceMessages.canNotPerformActionWhileExamIsOnThisStatus("strike examinee", exam.getExamStatus()));
        }

        int strikeCountForThisExam = examSecurityStrikeRepository.countByExamAndStrikeTypeAndCreatedBy(exam, securityStrikeType, examineeEmail);
        if(strikeCountForThisExam >= securityStrikeType.getMaxStrikeCount()) {
            examEnrollmentService.updateAttendanceStatus(exam, examineeEmail, AttendanceStatus.SUSPENDED);
            return AttendanceStatus.SUSPENDED;
        }

        ExamSecurityStrike examSecurityStrike = new ExamSecurityStrike()
                .setUuid(UUID.randomUUID())
                .setStrikeType(securityStrikeType)
                .setExamTime(Duration.between(exam.getActualStartDateTime(), LocalDateTime.now()))
                .setExam(exam)
                ;

        examSecurityStrikeRepository.save(examSecurityStrike);
        return AttendanceStatus.PRESENT;
    }

    public List<ExamSecurityStrike> getExamineeExamSecurityStrikes(Exam exam, String examineeEmail) {
        if(exam == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Exam"));
        }

        if(Util.isNullOrEmpty(examineeEmail)) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("AppUser.Email"));
        }

        return examSecurityStrikeRepository.findAllByExamAndCreatedBy(exam, examineeEmail);
    }
}
