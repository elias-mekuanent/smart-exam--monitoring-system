package com.senpare.examservice.service;

import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.exception.ForbiddenException;
import com.senpare.examservice.model.Exam;
import com.senpare.examservice.model.ExamEnrollment;
import com.senpare.examservice.model.enumeration.AttendanceStatus;
import com.senpare.examservice.model.enumeration.ExamStatus;
import com.senpare.examservice.repository.ExamEnrollmentRepository;
import com.senpare.examservice.utilities.ExamServiceMessages;
import com.senpare.examservice.utilities.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ExamEnrollmentService {

    private final ExamEnrollmentRepository examEnrollmentRepository;
    private final ExamService examService;

    @Transactional
    public Exam create(UUID examUuid, String examineeEmail) {
        if(examUuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Exam.UUID"));
        }

        if(Util.isNullOrEmpty(examineeEmail)) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNullOrEmpty("examineeEmail"));

        }

        Exam exam = examService.get(examUuid);
        if(exam.getExamStatus() != ExamStatus.STARTED) {
            throw new BadRequestException(ExamServiceMessages.canNotFetchResourceDueToExamStatus("Exam", exam.getExamStatus()));
        }

        boolean alreadyEnrolled = examEnrollmentRepository.existsByExamAndCreatedBy(exam, examineeEmail);
        if(alreadyEnrolled) {
            throw new ForbiddenException(ExamServiceMessages.alreadyEnrolled());
        }

        ExamEnrollment examEnrollment = new ExamEnrollment()
                .setUuid(UUID.randomUUID())
                .setExam(exam)
                .setToken(UUID.randomUUID().toString().substring(0, 8))
                .setAttendanceStatus(AttendanceStatus.PRESENT)
                ;
        examEnrollmentRepository.save(examEnrollment);

        return exam;
    }

    public void completedExamForExaminee(UUID examUUid, String examineeEmail) {
        updateAttendanceStatus(examService.get(examUUid), examineeEmail, AttendanceStatus.COMPLETED);
    }

    public void updateAttendanceStatus(Exam exam, String examineeEmail, AttendanceStatus attendanceStatus) {
        if(attendanceStatus == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamEnrollment.AttendanceStatus"));
        }

        if(Util.isNullOrEmpty(examineeEmail)) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("AppUser.Email"));
        }

        ExamEnrollment examEnrollment = examEnrollmentRepository.findFirstByExamAndCreatedBy(exam, examineeEmail)
                .orElseThrow(() -> new BadRequestException(ExamServiceMessages.canNotBeFound("ExamEnrollement", "exam and examineeEmail")));

        examEnrollment.setAttendanceStatus(attendanceStatus);
        examEnrollmentRepository.save(examEnrollment);
    }

    public boolean isExamineeEnrolled(Exam exam, String examineeEmail) {
        return examEnrollmentRepository.existsByExamAndCreatedBy(exam, examineeEmail);
    }

    public boolean isCurrentExamineeEnrolled(Exam exam) {
        String currentExamineeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return examEnrollmentRepository.existsByExamAndCreatedBy(exam, currentExamineeEmail);
    }

    public boolean isEnrollementActive(Exam exam) {
        String currentExamineeEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        ExamEnrollment examEnrollment = examEnrollmentRepository.findFirstByExamAndCreatedBy(exam, currentExamineeEmail)
                .orElseThrow(() -> new BadRequestException(ExamServiceMessages.canNotBeFound("ExamEnrollement", "exam and examineeEmail")));

        return examEnrollment.getAttendanceStatus() == AttendanceStatus.PRESENT;
    }
}
