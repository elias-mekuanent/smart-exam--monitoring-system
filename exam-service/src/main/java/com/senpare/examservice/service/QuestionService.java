package com.senpare.examservice.service;

import com.senpare.examservice.dto.QuestionRequest;
import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.exception.ForbiddenException;
import com.senpare.examservice.exception.ResourceNotFoundException;
import com.senpare.examservice.model.Exam;
import com.senpare.examservice.model.ExamSection;
import com.senpare.examservice.model.Question;
import com.senpare.examservice.model.enumeration.ExamStatus;
import com.senpare.examservice.repository.QuestionRepository;
import com.senpare.examservice.utilities.ExamServiceMessages;
import com.senpare.examservice.utilities.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamSectionService examSectionService;
    private final ExamEnrollmentService examEnrollmentService;

    @Transactional
    public Question create(UUID examSectionUuid, QuestionRequest questionRequest) {
        if(examSectionUuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("QuestionRequest.examSectionUuid"));
        }

        if(questionRequest == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("QuestionRequest"));
        }

        Question question = createQuestionFromRequest(questionRequest);
        ExamSection examSection = examSectionService.get(examSectionUuid);
        question.setExamSection(examSection);

        return questionRepository.save(question);
    }

    public Question get(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Question.uuid"));
        }

        return questionRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("Question", "uuid", uuid.toString())));
    }

    public Question getForExaminee(UUID uuid) {
        Question question = get(uuid);
        Exam exam = question.getExamSection().getExam();

        if(exam.getExamStatus() == ExamStatus.STARTED) {
            return question;
        }

        boolean currentExamineeEnrolled = examEnrollmentService.isEnrollementActive(exam);
        if(!currentExamineeEnrolled) {
            throw new ForbiddenException(ExamServiceMessages.enrollmentNotActive());
        }

        throw new BadRequestException(ExamServiceMessages.canNotFetchResourceDueToExamStatus("Question", exam.getExamStatus()));
    }

    public Page<Question> getAll(int page, int size, String sort, UUID examSectionUuid) {
        if(examSectionUuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Question.examSectionUuid"));
        }

        ExamSection examSection = examSectionService.get(examSectionUuid);
        Pageable pageRequest = Util.getPageable(page, size, sort);

        return questionRepository.findAllByExamSection(pageRequest, examSection);
    }

    public Page<Question> getAllForExaminee(int page, int size, String sort, UUID examSectionUuid) {
        if(examSectionUuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Question.examSectionUuid"));
        }

        ExamSection examSection = examSectionService.get(examSectionUuid);
        Exam exam = examSection.getExam();

        boolean currentExamineeEnrolled = examEnrollmentService.isEnrollementActive(exam);
        if(!currentExamineeEnrolled) {
            throw new ForbiddenException(ExamServiceMessages.enrollmentNotActive());
        }

        if(exam.getExamStatus() == ExamStatus.STARTED) {
            Pageable pageRequest = Util.getPageable(page, size, sort);
            return questionRepository.findAllByExamSection(pageRequest, examSection);
        }

        throw new BadRequestException(ExamServiceMessages.canNotFetchResourceDueToExamStatus("Question", exam.getExamStatus()));
    }

    @Transactional
    public Question update(UUID uuid, QuestionRequest request) {
        if(uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Question.uuid"));
        }

        if(request == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("QuestionRequest"));
        }

        Question question = questionRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("Question", "uuid", uuid.toString())));

        toQuestion(question, request);

        return questionRepository.save(question);
    }

    @Transactional
    public Question delete(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Question.uuid"));
        }

        Question question = questionRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("Question", "uuid", uuid.toString())));

        questionRepository.delete(question);

        return question;
    }

    private Question createQuestionFromRequest(QuestionRequest request) {
        return new Question()
                .setUuid(UUID.randomUUID())
                .setTitle(request.getTitle())
                .setOptionOrderType(request.getOptionOrderType());

    }

    private void toQuestion(Question question, QuestionRequest request) {
        if(Util.isNotNullAndEmpty(request.getTitle())) {
            question.setTitle(request.getTitle());
        }

        if(Util.isNotNullAndEmpty(request.getOptionOrderType())) {
            question.setOptionOrderType(request.getOptionOrderType());
        }
    }
}
