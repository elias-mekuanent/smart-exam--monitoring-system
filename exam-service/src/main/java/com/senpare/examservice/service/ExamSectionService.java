package com.senpare.examservice.service;

import com.senpare.examservice.dto.ExamSectionRequest;
import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.exception.ForbiddenException;
import com.senpare.examservice.exception.ResourceNotFoundException;
import com.senpare.examservice.model.Exam;
import com.senpare.examservice.model.ExamSection;
import com.senpare.examservice.model.enumeration.ExamStatus;
import com.senpare.examservice.repository.ExamSectionRepository;
import com.senpare.examservice.utilities.ExamServiceMessages;
import com.senpare.examservice.utilities.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamSectionService {

    private final ExamEnrollmentService examEnrollmentService;
    private final ExamService examService;
    private final ExamSectionRepository examSectionRepository;

    @Transactional
    public ExamSection create(UUID examUuid, ExamSectionRequest request) {
        if(request == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSectionRequest"));
        }

        ExamSection examSection = createExamSectionFromRequest(request);

        Exam exam = examService.get(examUuid);
        examSection.setExam(exam);

        return examSectionRepository.save(examSection);
    }

    public ExamSection get(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSection.uuid"));
        }

        return examSectionRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("ExamSection", "uuid", uuid.toString())));
    }

    public ExamSection getForExaminee(UUID uuid) {
        ExamSection examSection = get(uuid);

        Exam exam = examSection.getExam();
        if(exam.getExamStatus() != ExamStatus.STARTED) {
            throw new BadRequestException(ExamServiceMessages.canNotFetchResourceDueToExamStatus("ExamSection", exam.getExamStatus()));
        }

        boolean currentExamineeEnrolled = examEnrollmentService.isEnrollementActive(exam);
        if(!currentExamineeEnrolled) {
            throw new ForbiddenException(ExamServiceMessages.enrollmentNotActive());
        }

        return examSection;
    }

    public Page<ExamSection> getAll(int page, int size, String sort, UUID examUuid) {
        if(examUuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSection.Exam.uuid"));
        }
        Exam exam = examService.get(examUuid);

        Pageable pageRequest = Util.getPageable(page, size, sort);
        return examSectionRepository.findAllByExam(exam, pageRequest == null ? Pageable.unpaged() : pageRequest);
    }

    public List<ExamSection> getAll(UUID examUuid) {
        if(examUuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSection.Exam.uuid"));
        }
        Exam exam = examService.get(examUuid);

        return examSectionRepository.findAllByExam(exam);
    }

    public Page<ExamSection> getAllForExaminee(int page, int size, String sort, UUID examUuid) {
        if(examUuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSection.Exam.uuid"));
        }

        Exam exam = examService.get(examUuid);
        if(exam.getExamStatus() != ExamStatus.STARTED) {
            throw new BadRequestException(ExamServiceMessages.canNotFetchResourceDueToExamStatus("ExamSection", exam.getExamStatus()));
        }

        boolean currentExamineeEnrolled = examEnrollmentService.isEnrollementActive(exam);
        if(!currentExamineeEnrolled) {
            throw new ForbiddenException(ExamServiceMessages.isNotEnrolled());
        }

        Pageable pageRequest = Util.getPageable(page, size, sort);
        return examSectionRepository.findAllByExam(exam, pageRequest == null ? Pageable.unpaged() : pageRequest);
    }

    @Transactional
    public ExamSection update(UUID uuid, ExamSectionRequest request) {
        if(uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSection.uuid"));
        }

        if(request == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSection"));
        }

        ExamSection examSection = examSectionRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("ExamSection", "uuid", uuid.toString())));

        toExamSection(examSection, request);

        return examSectionRepository.save(examSection);
    }

    public ExamSection delete(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSection.uuid"));
        }

        ExamSection examSection = examSectionRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("ExamSection", "uuid", uuid.toString())));

        //TODO: check if there are questions in this section.

        examSectionRepository.delete(examSection);

        return examSection;
    }

    public void toExamSection(ExamSection examSection, ExamSectionRequest request) {
        if(Util.isNotNullAndEmpty(request.getTitle())) {
            examSection.setTitle(request.getTitle());
        }

        if(Util.isNotNullAndEmpty(request.getExamSectionType())) {
            examSection.setExamSectionType(request.getExamSectionType());
        }

        if(request.getWeightPerQuestion() != null) {
            examSection.setWeightPerQuestion(request.getWeightPerQuestion());
        }

    }

    public ExamSection createExamSectionFromRequest(ExamSectionRequest request) {
        return new ExamSection()
                .setUuid(UUID.randomUUID())
                .setTitle(request.getTitle())
                .setExamSectionType(request.getExamSectionType())
                .setWeightPerQuestion(request.getWeightPerQuestion())
                ;
    }
}
