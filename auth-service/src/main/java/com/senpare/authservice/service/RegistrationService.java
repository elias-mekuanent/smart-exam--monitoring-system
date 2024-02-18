package com.senpare.authservice.service;

import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.job.JobTemplateImpl;
import com.northconcepts.datapipeline.memory.MemoryWriter;
import com.senpare.authservice.client.ExamClient;
import com.senpare.authservice.client.response.Exam;
import com.senpare.authservice.dto.AppUserRequest;
import com.senpare.authservice.dto.EmailDTO;
import com.senpare.authservice.dto.ExamineeRegistrationRequest;
import com.senpare.authservice.dto.ExaminerRegistrationRequest;
import com.senpare.authservice.exception.BadRequestException;
import com.senpare.authservice.exception.ResourceAlreadyExistsException;
import com.senpare.authservice.kafka.KafkaProducer;
import com.senpare.authservice.model.AppUser;
import com.senpare.authservice.model.ConfirmationToken;
import com.senpare.authservice.utilities.AuthServiceMessages;
import com.senpare.authservice.utilities.EmailTemplate;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class RegistrationService {

    private final AppUserService appUserService;
    private final AppUserRoleService appUserRoleService;
    private final AppUserImageService appUserImageService;
    private final ExamClient examClient;
    private final KafkaProducer producer;
    private final EmailTemplate emailTemplate;
    private final ConfirmationTokenService confirmationTokenService;
    private final String frontendUrl;
    private final int tokenExpirationDelay;
    private final Clock clock;

    public RegistrationService(AppUserService appUserService,
                               AppUserRoleService appUserRoleService,
                               AppUserImageService appUserImageService,
                               ExamClient examClient,
                               KafkaProducer producer,
                               EmailTemplate emailTemplate,
                               ConfirmationTokenService confirmationTokenService,
                               @Value(value = "${app.front-end-url}") String frontendUrl,
                               @Value(value = "${app.token-expiration-delay}") int verificationExpireDelay,
                               Clock clock) {

        this.appUserService = appUserService;
        this.appUserRoleService = appUserRoleService;
        this.appUserImageService = appUserImageService;
        this.examClient = examClient;
        this.producer = producer;
        this.emailTemplate = emailTemplate;
        this.confirmationTokenService = confirmationTokenService;
        this.frontendUrl = frontendUrl;
        this.tokenExpirationDelay = verificationExpireDelay;
        this.clock = clock;
    }

    public AppUser registerExaminer(final ExaminerRegistrationRequest request) {
        AppUser appUser  = appUserService.create(request, false);
        appUserRoleService.assignRoleByRoleNames(appUser.getUuid(), List.of("ROLE_EXAMINER"));

        appUser.setPhoneNumber(request.getPhoneNumber())
                .setAccountType(request.getAccountType())
                .setOrganization(request.getOrganization())
                .setRoleInOrganization(request.getRoleInOrganization())
                ;

        final String token = createConfirmationToken(appUser);

        final String link = frontendUrl + "/confirm-email?token=" + token;
        producer.pushEmailToKafka(new EmailDTO()
                        .setTo(appUser.getEmail())
                        .setSubject("Confirm your email")
                        .setBody(emailTemplate.buildConfirmationEmail(appUser.getFirstName() + " " + appUser.getLastName(), link))
        );
        return appUser;
    }

    @Transactional
    public AppUser registerExaminee(final ExamineeRegistrationRequest request, byte[] image) {
        if(request == null) {
            throw new BadRequestException(AuthServiceMessages.canNotBeNull("ExamineeRegistrationRequest"));
        }

        Exam exam = examClient.getExam(request.getExamUuid());
        validateEmail(exam);

        String generatedPassword = UUID.randomUUID().toString().substring(0, 8);
        request.setPassword(generatedPassword);
        AppUser appUser  = appUserService.create(request, true);
        appUser.setExamUUid(request.getExamUuid());
        appUserRoleService.assignRoleByRoleNames(appUser.getUuid(), List.of("ROLE_EXAMINEE"));

        if(image !=  null && exam.getExamSetting().isIncludeCameraSecurity() && image.length > 0) {
            log.info("Image saved for appUser");
            appUserImageService.create(appUser, image);
        }

        String examLink = frontendUrl + "/examinee/" + exam.getUuid();
        producer.pushEmailToKafka(new EmailDTO()
                .setTo(appUser.getEmail())
                .setSubject("Login Details for Exam System")
                .setBody(emailTemplate.buildExamInvitationEmail(appUser.getFirstName() + " " + appUser.getLastName(), request.getEmail(), generatedPassword, examLink))
        );

        return appUser;
    }

    @Transactional
    public void bulkImport(MultipartFile multipartFile, UUID examUuid) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();

        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream outputStream = new FileOutputStream(file);

        outputStream.write(inputStream.readAllBytes());

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        CSVReader csvReader = new CSVReader(file).setFieldNamesInFirstRow(true);
        RecordList recordList = new RecordList();

        MemoryWriter memoryWriter = new MemoryWriter(recordList);

        Job.run(csvReader, memoryWriter);

        recordList.stream()
                .forEach(record -> {
                    ExamineeRegistrationRequest request = new ExamineeRegistrationRequest()
                            .setFirstName(record.getFieldValueAsString("firstName", null))
                            .setLastName(record.getFieldValueAsString("lastName", null))
                            .setEmail(record.getFieldValueAsString("email", null))
                            .setExamUuid(examUuid);
                    registerExaminee(request, null);
                });
    }



    public void confirmToken(final String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);
        AppUser appUser = confirmationToken.getAppUser();

        if(confirmationToken.getConfirmedAt() != null) {
            throw new ResourceAlreadyExistsException(AuthServiceMessages.alreadyConfirmed("Token"));
        }

        if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now(clock))) {
            throw new BadRequestException(AuthServiceMessages.alreadyExpired("Token"));
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableUser(appUser);
    }

     public String createConfirmationToken(AppUser appUser) {
        ConfirmationToken createdConfirmationToken =  confirmationTokenService.create(
              new ConfirmationToken()
                        .setToken(UUID.randomUUID().toString())
                        .setAppUser(appUser)
                        .setExpiresAt(LocalDateTime.now(clock).plusMinutes(tokenExpirationDelay))
        );
        return createdConfirmationToken.getToken();
    }

    @Transactional
    public void resendToken(UUID appUserUuid) {
        AppUser appUser = appUserService.getAppUser(appUserUuid);

        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenByAppUser(appUser);
        if(confirmationToken.getConfirmedAt() != null) {
            throw new BadRequestException(AuthServiceMessages.alreadyConfirmed("Token"));
        }

        confirmationTokenService.delete(confirmationToken.getUuid());
        String token = createConfirmationToken(appUser);

        sendConfirmationEmail(token, appUser);
    }

    private void sendConfirmationEmail(final String token, final AppUser appUser) {
        final String link = frontendUrl + "/confirm-email?token=" + token;
        producer.pushEmailToKafka(new EmailDTO()
                .setTo(appUser.getEmail())
                .setSubject("Confirm your email")
                .setBody(emailTemplate.buildConfirmationEmail(appUser.getFirstName() + " " + appUser.getLastName(), link))
        );
    }

    private AppUserRequest mapToAppUserRequest(ExaminerRegistrationRequest request) {
        return new AppUserRequest()
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setEmail(request.getEmail())
                .setPassword(request.getPassword());
    }

    private void validateEmail(Exam exam) {
        int examineeCount = appUserService.countExamineeInExam(exam.getUuid().toString());
        int allowedExamineeCount = exam.getExamSetting().getExamineeCount();

        if(examineeCount >= allowedExamineeCount) {
            throw new BadRequestException(AuthServiceMessages.exceedsMaxExamineeCount(allowedExamineeCount));
        }

        if(!exam.getExamStatus().equalsIgnoreCase("created")) {
            throw new BadRequestException(AuthServiceMessages.canNotAddExamineeToExamWithThisStatus(exam.getExamStatus()));
        }
    }

}
