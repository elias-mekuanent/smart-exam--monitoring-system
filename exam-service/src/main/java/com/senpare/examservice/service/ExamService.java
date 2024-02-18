package com.senpare.examservice.service;

import com.senpare.examservice.client.LicenseClient;
import com.senpare.examservice.client.response.License;
import com.senpare.examservice.client.response.LicenseType;
import com.senpare.examservice.dto.ExamRequest;
import com.senpare.examservice.dto.ExamSettingRequest;
import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.exception.LicenseViolationException;
import com.senpare.examservice.exception.ResourceNotFoundException;
import com.senpare.examservice.model.Exam;
import com.senpare.examservice.model.ExamSetting;
import com.senpare.examservice.model.enumeration.ExamStatus;
import com.senpare.examservice.repository.ExamRepository;
import com.senpare.examservice.utilities.ExamServiceMessages;
import com.senpare.examservice.utilities.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamService {

    private static final int DEFAULT_MAXIMUM_SECURITY_STRIKE = 1;
    private final ExamRepository examRepository;
    private final LicenseClient licenseClient;
    private final ExamSettingService examSettingService;

    @Transactional
    public Exam create(ExamRequest request) {
        if (request == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamRequest"));
        }

        Exam exam = createExamFromRequest(request);
        validateLicense(exam.getLicenseUuid());

        License license = licenseClient.getLicense(exam.getLicenseUuid().toString());
        ExamSetting defaultExamSetting = createDefaultExamSetting(license);
        exam.setExamSetting(defaultExamSetting);

        examRepository.save(exam);

        useLicense(exam.getLicenseUuid());

        return exam;
    }

    public Exam get(UUID uuid) {
        if (uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamRequest.uuid"));
        }

        return examRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("Exam", "uuid", uuid.toString())));
    }

    public Page<Exam> getAll(int page, int size, String sortBy) {
        return Util.paginate(examRepository, page, size, sortBy);
    }

    public Page<Exam> getAllExamsForExaminer(String createdBy, int page, int size, String sortBy) {
        Pageable pageable = Util.getPageable(page, size, sortBy);
        return examRepository.findAllByCreatedBy(createdBy, pageable);
    }

    @Transactional
    public Exam update(UUID uuid, ExamRequest request) {
        if (uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Exam.uuid"));
        }

        if (request == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamRequest"));
        }

        Exam existingExam = examRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("Exam", "uuid", uuid.toString())));

        if (!existingExam.getExamStatus().equals(ExamStatus.CREATED)) {
            throw new BadRequestException(ExamServiceMessages.canNotDeleteExamWithThisStatus(existingExam.getExamStatus()));
        }

        toExam(existingExam, request);

        return existingExam;
    }


    @Transactional
    public Exam delete(UUID uuid) {
        if (uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Exam.uuid"));
        }

        Exam exam = examRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("Exam", "uuid", uuid.toString())));

        if (exam.getExamStatus().equals(ExamStatus.STARTED)) {
            throw new BadRequestException(ExamServiceMessages.canNotDeleteExamWithThisStatus(exam.getExamStatus()));
        }

        examRepository.delete(exam);

        return exam;
    }

    @Transactional
    public Exam updateExamSetting(UUID uuid, ExamSettingRequest examSettingRequest) {
        if (uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Exam.uuid"));
        }

        Exam exam = examRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("Exam", "uuid", uuid.toString())));

        examSettingService.update(exam.getExamSetting().getUuid(), examSettingRequest);
        return exam;
    }

    public Exam updateStatus(UUID uuid, ExamStatus examStatus) {
        if (uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Exam.uuid"));
        }

        Exam exam = examRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("Exam", "uuid", uuid.toString())));

        // exam status can't be manually updated to CREATED
        if (examStatus == ExamStatus.CREATED) {
            throw new BadRequestException(ExamServiceMessages.canNotUpdateExamToThisStatus(exam.getExamStatus()));
        }

        // exam status should be CREATED or CANCELED to start an exam
        if (examStatus == ExamStatus.STARTED && (exam.getExamStatus() != ExamStatus.CREATED && exam.getExamStatus() != ExamStatus.CANCELED)) {
            throw new BadRequestException(ExamServiceMessages.canNotStartExamWithThisStatus(exam.getExamStatus()));
        }

        // can't cancel started exam
        if (examStatus == ExamStatus.CANCELED && exam.getExamStatus() == ExamStatus.STARTED) {
            throw new BadRequestException(ExamServiceMessages.canNotStartExamWithThisStatus(exam.getExamStatus()));
        }

        // exam status should be STARTED to complete/close an exam
        if (examStatus == ExamStatus.COMPLETED && exam.getExamStatus() != ExamStatus.STARTED) {
            throw new BadRequestException(ExamServiceMessages.canNotCompleteExamWithThisStatus(exam.getExamStatus()));
        }

        exam.setExamStatus(examStatus);
        final LocalDateTime now = LocalDateTime.now();
        if(examStatus == ExamStatus.STARTED) {
            exam.setActualStartDateTime(now);
        } else if (examStatus == ExamStatus.COMPLETED) {
            exam.setActualDuration(Duration.between(now, exam.getActualStartDateTime()));
            exam.setActualStartDateTime(LocalDateTime.now());
        }

        return examRepository.save(exam);
    }

    public Exam createExamFromRequest(ExamRequest request) {
        return new Exam()
                .setUuid(UUID.randomUUID())
                .setLicenseUuid(request.getLicenseUuid())
                .setTitle(request.getTitle())
                .setDescription(request.getDescription())
                .setExamInstruction(request.getExamInstruction())
                .setExamType(request.getExamType())
                .setPlannedStartDateTime(request.getPlannedStartDateTime())
                .setPlannedDuration(Duration.ofMinutes(request.getPlannedDuration()))
                .setExamStatus(ExamStatus.CREATED)
//                .setCategory(request.getCategory())
                ;
    }

    private void toExam(Exam exam, ExamRequest request) {
        if (Util.isNotNullAndEmpty(request.getLicenseUuid())) {
            validateLicense(UUID.fromString(request.getLicenseUuid()));
            exam.setLicenseUuid(request.getLicenseUuid());
        }

        if (Util.isNotNullAndEmpty(request.getTitle())) {
            exam.setTitle(request.getTitle());
        }

        if (Util.isNotNullAndEmpty(request.getDescription())) {
            exam.setDescription(request.getDescription());
        }

        if (Util.isNotNullAndEmpty(request.getExamInstruction())) {
            exam.setExamInstruction(request.getExamInstruction());
        }

        if (request.getExamType() != null) {
            exam.setExamType(request.getExamType());
        }

        if (request.getPlannedStartDateTime() != null) {
            exam.setPlannedStartDateTime(request.getPlannedStartDateTime());
        }

        if (request.getPlannedDuration() != null) {
            exam.setPlannedDuration(Duration.ofMinutes(request.getPlannedDuration()));
        }

//        if(request.getCategory() != null) {
//            exam.setCategory(request.getCategory());
//        }
    }

    private void validateLicense(UUID licenseUuid) {
        boolean isLicenseValid = licenseClient.validateLicense(licenseUuid.toString());

        if (!isLicenseValid) {
            throw new LicenseViolationException(ExamServiceMessages.invalidLicense());
        }
    }

    private License useLicense(UUID uuid) {
        return licenseClient.useLicense(uuid.toString());
    }

    private ExamSetting createDefaultExamSetting(License license) {
        LicenseType licenseType = license.getLicenseType();

        ExamSettingRequest examSettingRequest = new ExamSettingRequest()
                .setIncludeMockExam(true)
                .setExamineeCount(licenseType.getAllowedExamineeCount())
                .setIncludeCameraSecurity(licenseType.getCameraSecurity())
                .setIncludeScreenSecurity(licenseType.getScreenshotSecurity())
                .setIncludeMouseTrackSecurity(licenseType.getMouseTrackSecurity())
                .setIncludeVoiceRecordSecurity(licenseType.getAudioRecordSecurity())
                .setShuffleQuestion(true)
                .setAutoGrade(true)
                .setMaxSecurityStrike(DEFAULT_MAXIMUM_SECURITY_STRIKE);
        return examSettingService.create(license.getUuid(), examSettingRequest);
    }

    public int countByStatus(ExamStatus status) {
        if(status == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamStatus"));
        }
        return examRepository.countByExamStatus(status);
    }

}
