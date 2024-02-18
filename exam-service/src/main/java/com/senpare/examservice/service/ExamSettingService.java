package com.senpare.examservice.service;

import com.senpare.examservice.client.LicenseClient;
import com.senpare.examservice.client.response.License;
import com.senpare.examservice.client.response.LicenseType;
import com.senpare.examservice.dto.ExamSettingRequest;
import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.exception.LicenseViolationException;
import com.senpare.examservice.model.ExamSetting;
import com.senpare.examservice.repository.ExamRepository;
import com.senpare.examservice.repository.ExamSettingRepository;
import com.senpare.examservice.utilities.ExamServiceMessages;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamSettingService {

    private final ExamSettingRepository examSettingRepository;
    private final LicenseClient licenseClient;

    @Transactional
    public ExamSetting create(UUID licenseUuid, ExamSettingRequest request) {
        if(request == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSettingRequest"));
        }

        if(licenseUuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("License.uuid"));
        }

        ExamSetting examSetting = createExamSettingFromRequest(request);

        validateExamSetting(licenseUuid, examSetting);

        return examSettingRepository.save(examSetting);
    }

    public ExamSetting get(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSetting.uuid"));
        }

        return examSettingRepository.findById(uuid)
                .orElseThrow(() -> new BadRequestException(ExamServiceMessages.canNotBeFound("ExamSetting", "uuid", uuid.toString())));
    }

    public ExamSetting getByExam(UUID examUuid) {
        if(examUuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSetting.examUuid"));
        }

        return examSettingRepository.findByExam(examUuid)
                .orElseThrow(() -> new BadRequestException(ExamServiceMessages.canNotBeFound("ExamSetting", "uuid", examUuid.toString())));
    }

    @Transactional
    public ExamSetting update(UUID uuid, ExamSettingRequest request) {
        if(uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSetting.uuid"));
        }

        if(request == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSettingRequest"));
        }

        ExamSetting existingExamSetting = examSettingRepository.findById(uuid)
                .orElseThrow(() -> new BadRequestException(ExamServiceMessages.canNotBeFound("ExamSetting", "uuid", uuid.toString())));

        toExamSetting(existingExamSetting, request);

        validateExamSetting(existingExamSetting.getExam().getLicenseUuid(), existingExamSetting);

        return examSettingRepository.save(existingExamSetting);
    }

    public ExamSetting createExamSettingFromRequest(ExamSettingRequest request) {
        return new ExamSetting()
                .setUuid(UUID.randomUUID())
                .setExamineeCount(request.getExamineeCount())
                .setIncludeMockExam(request.getIncludeMockExam())
                .setIncludeCameraSecurity(request.getIncludeCameraSecurity())
                .setIncludeMouseTrackSecurity(request.getIncludeMouseTrackSecurity())
                .setIncludeCameraSecurity(request.getIncludeCameraSecurity())
                .setIncludeScreenSecurity(request.getIncludeScreenSecurity())
                .setIncludeVoiceRecordSecurity(request.getIncludeVoiceRecordSecurity())
                .setMaxSecurityStrike(request.getMaxSecurityStrike())
                .setAutoGrade(request.getAutoGrade())
                .setShuffleQuestion(request.getShuffleQuestion())
                ;
    }

    private void toExamSetting(ExamSetting existingExamSetting, ExamSettingRequest request) {
        if(request.getExamineeCount() != null) {
            existingExamSetting.setExamineeCount(request.getExamineeCount());
        }

        if(request.getIncludeMockExam() != null) {
            existingExamSetting.setIncludeMockExam(request.getIncludeMockExam());
        }

        if(request.getIncludeCameraSecurity() != null) {
            existingExamSetting.setIncludeCameraSecurity(request.getIncludeCameraSecurity());
        }

        if(request.getIncludeMouseTrackSecurity() != null) {
            existingExamSetting.setIncludeMouseTrackSecurity(request.getIncludeMouseTrackSecurity());
        }

        if(request.getIncludeScreenSecurity() != null) {
            existingExamSetting.setIncludeScreenSecurity(request.getIncludeScreenSecurity());
        }

        if(request.getIncludeVoiceRecordSecurity() != null) {
            existingExamSetting.setIncludeVoiceRecordSecurity(request.getIncludeVoiceRecordSecurity());
        }

        if(request.getMaxSecurityStrike() != null) {
            existingExamSetting.setMaxSecurityStrike(request.getMaxSecurityStrike());
        }

        if(request.getAutoGrade() != null) {
            existingExamSetting.setAutoGrade(request.getAutoGrade());
        }

        if(request.getShuffleQuestion() != null) {
            existingExamSetting.setShuffleQuestion(request.getShuffleQuestion());
        }
    }

    private void validateExamSetting(UUID licenseUuid, ExamSetting examSetting) {
        License license = licenseClient.getLicense(licenseUuid.toString());
        if(license == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeFound("License", "uuid", licenseUuid.toString()));
        }

        LicenseType licenseType = license.getLicenseType();

        if(examSetting.isIncludeCameraSecurity()) {
            if(!licenseType.getCameraSecurity()) {
                throw new LicenseViolationException(ExamServiceMessages.actionNotAllowedInThisLicense("Camera Security"));
            }
        }

        if(examSetting.isIncludeVoiceRecordSecurity()) {
            if(!licenseType.getAudioRecordSecurity()) {
                throw new LicenseViolationException(ExamServiceMessages.actionNotAllowedInThisLicense("Voice Security"));

            }
        }

        if(examSetting.isIncludeScreenSecurity()) {
            if(!licenseType.getScreenshotSecurity()) {
                throw new LicenseViolationException(ExamServiceMessages.actionNotAllowedInThisLicense("Browser screenshot Security"));
            }
        }

        if(examSetting.isIncludeMouseTrackSecurity()) {
            if(!licenseType.getMouseTrackSecurity()) {
                throw new LicenseViolationException(ExamServiceMessages.actionNotAllowedInThisLicense("Mouse track Security"));
            }
        }

        if (examSetting.getExamineeCount() > licenseType.getAllowedExamineeCount()) {
            throw new LicenseViolationException(ExamServiceMessages.examineeCountExceedsLicenseLimit(licenseType.getAllowedExamineeCount()));
        }

    }

}
