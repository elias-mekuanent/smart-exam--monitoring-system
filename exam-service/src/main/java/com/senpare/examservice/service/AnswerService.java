package com.senpare.examservice.service;

import com.senpare.examservice.client.AppUserClient;
import com.senpare.examservice.client.response.AppUser;
import com.senpare.examservice.dto.AnswerDTO;
import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.exception.ForbiddenException;
import com.senpare.examservice.exception.ResourceNotFoundException;
import com.senpare.examservice.model.*;
import com.senpare.examservice.model.enumeration.ExamStatus;
import com.senpare.examservice.repository.AnswerRepository;
import com.senpare.examservice.utilities.ExamServiceMessages;
import com.senpare.examservice.utilities.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerService {

    private final AppUserClient appUserClient;
    private final ExamEnrollmentService examEnrollmentService;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final AnswerRepository answerRepository;

    public void createAndUpdate(AnswerDTO answerDTO) {
        Question question = questionService.get(answerDTO.getQuestionUUid());
        Option option = optionService.get(answerDTO.getOptionUuid());

        Exam exam = question.getExamSection().getExam();
        if(!examEnrollmentService.isEnrollementActive(exam)) {
            throw new ForbiddenException(ExamServiceMessages.enrollmentNotActive());
        }

        if(exam.getExamStatus() != ExamStatus.STARTED) {
            throw new ForbiddenException(ExamServiceMessages.canNotPerformActionWhileExamIsOnThisStatus("answer questions", exam.getExamStatus()));
        }

        String currentExamineeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        int answerCount = answerRepository.countByCreatedByAndQuestion(currentExamineeEmail, question);


        Time timeTaken = Time.valueOf(LocalTime.now().minusMinutes(exam.getActualStartDateTime().toLocalTime().getMinute()));

        Answer answer = new Answer();
        if(answerCount > 0) {
            log.debug("Updating answer");

            Answer previousAnswer = answerRepository.findFirstByCreatedByAndQuestion(currentExamineeEmail, question)
                    .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("Answer", "currentExamineeEmail and question")));
            answer.setUuid(previousAnswer.getUuid())
                    .setQuestion(question)
                    .setOption(option)
                    .setTimeTaken(timeTaken)
                    .setAttempt(previousAnswer.getAttempt() + 1)
                    .setCorrectAnswer(option.isCorrectOption());
        } else {
            log.debug("Creating answer");

            answer.setUuid(UUID.randomUUID())
                    .setQuestion(question)
                    .setOption(option)
                    .setTimeTaken(timeTaken)
                    .setAttempt(1)
                    .setCorrectAnswer(option.isCorrectOption());
        }

        answerRepository.save(answer);
    }

    public String getAnswerOptionByQuestion(UUID questionUuid) {
        if(questionUuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Question.UUID"));
        }

        Question question = questionService.get(questionUuid);
        String currentExamineeEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Answer> selectedAnswer = answerRepository.findFirstByCreatedByAndQuestion(currentExamineeEmail, question);

        return selectedAnswer.isPresent() ? selectedAnswer.get().getOption().getUuid().toString() : null;
    }

    public int countCorrectAnswersByExamSectionAndCreatedBy(ExamSection examSection, String examneeEmail) {
        if(examSection == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("ExamSection"));
        }

        if(Util.isNullOrEmpty(examneeEmail)) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNullOrEmpty("AppUser.email"));
        }

        return answerRepository.countCorrectAnswersByExamSectionAndCreatedBy(examSection, examneeEmail);
    }

    private AppUser getAppUser() {
        return appUserClient.getAppUser();
    }
}
