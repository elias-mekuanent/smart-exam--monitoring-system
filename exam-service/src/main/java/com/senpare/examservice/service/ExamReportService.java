package com.senpare.examservice.service;

import com.google.common.util.concurrent.AtomicDouble;
import com.senpare.examservice.client.AppUserClient;
import com.senpare.examservice.client.response.AppUser;
import com.senpare.examservice.dto.ExamReport;
import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.model.Exam;
import com.senpare.examservice.model.ExamSection;
import com.senpare.examservice.model.ExamSecurityStrike;
import com.senpare.examservice.model.enumeration.AttendanceStatus;
import com.senpare.examservice.model.enumeration.ExamStatus;
import com.senpare.examservice.utilities.ExamServiceMessages;
import com.senpare.examservice.utilities.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamReportService {

    private final ExamEnrollmentService examEnrollmentService;
    private final ExamService examService;
    private final ExamSectionService examSectionService;
    private final AnswerService answerService;
    private final ExamSecurityStrikeService examSecurityStrikeService;

    private final AppUserClient appUserClient;

    public ExamReport getExamineeExamReport(UUID examUuid, UUID examineeUuid) {
        Exam exam = examService.get(examUuid);

        if(exam.getExamStatus() != ExamStatus.COMPLETED) {
            throw new BadRequestException(ExamServiceMessages.canNotPerformActionWhileExamIsOnThisStatus("generate action", exam.getExamStatus()));
        }

        AppUser appUser = appUserClient.getAppUserByUuid(examineeUuid);
        ExamReport examReport = new ExamReport()
                .setExamUuid(examUuid);
        if(!examEnrollmentService.isExamineeEnrolled(exam, appUser.getEmail())) {
            examReport.setTotalScore(0)
                    .setExaminee(appUser.getEmail())
                    .setAttendanceStatus(AttendanceStatus.ABSENT);
            return examReport;
        }

        List<ExamSection> examSections = examSectionService.getAll(examUuid);
        AtomicDouble totalScore = new AtomicDouble();
        examSections.stream()
                .forEach(examSection -> {
                    int count = answerService.countCorrectAnswersByExamSectionAndCreatedBy(examSection, appUser.getEmail());
                    totalScore.addAndGet(examSection.getWeightPerQuestion() * count);
                });

        List<ExamSecurityStrike> securityStrikes = examSecurityStrikeService.getExamineeExamSecurityStrikes(exam, appUser.getEmail());
        examReport.setTotalScore(totalScore.doubleValue())
                .setExaminee(appUser.getEmail())
                .setAttendanceStatus(AttendanceStatus.PRESENT)
                .setSecurityStrikes(Util.toExamSecurityStrikeDTOs(securityStrikes));

        return examReport;
    }

    public List<ExamReport> getExamineesExamReport(int page, int size, String sort, UUID examUuid) {
        List<AppUser> appUsers = appUserClient.getExaminees(page, size, sort, examUuid.toString());

        List<ExamReport> examReports = appUsers.stream()
                .map(appUser -> getExamineeExamReport(examUuid, appUser.getUuid()))
                .collect(Collectors.toList());

        return examReports;

    }

}
