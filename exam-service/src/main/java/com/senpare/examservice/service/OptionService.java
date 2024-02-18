package com.senpare.examservice.service;

import com.senpare.examservice.dto.OptionRequest;
import com.senpare.examservice.exception.BadRequestException;
import com.senpare.examservice.exception.ResourceNotFoundException;
import com.senpare.examservice.model.Option;
import com.senpare.examservice.model.Question;
import com.senpare.examservice.repository.OptionRepository;
import com.senpare.examservice.utilities.ExamServiceMessages;
import com.senpare.examservice.utilities.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;
    private final QuestionService questionService;

    @Transactional
    public Option create(UUID questionUuid, OptionRequest optionRequest) {
        if(optionRequest == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("optionRequest"));
        }

        Option option = createOptionFromRequest(optionRequest);

        Question question = questionService.get(questionUuid);
        option.setQuestion(question);

        // XXXX: remove this to allow question with multiple correct options
        if(option.isCorrectOption()) {
            int correctOptionCount = optionRepository.countCorrectOptionsInQuestion(question);
            if(correctOptionCount > 0) {
                throw new BadRequestException(ExamServiceMessages.canNotDuplicateResource("correct options", "question"));
            }
        }

        return optionRepository.save(option);
    }

    public Option get(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Option.uuid"));
        }

        return optionRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(ExamServiceMessages.canNotBeFound("Option", "uuid", uuid.toString())));
    }

    public List<Option> getAll(UUID questionUuid) {
        if(questionUuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Question.uuid"));
        }

        Question question = questionService.get(questionUuid);
        return optionRepository.findAllByQuestion(question);
    }

    @Transactional
    public Option update(UUID uuid, OptionRequest request) {
        if(uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Option.uuid"));
        }

        if(request == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("OptionRequest"));
        }

        Option existingOption = get(uuid);

        // XXXX: remove this to allow question with multiple correct options
        if(request.getCorrectOption() != null && request.getCorrectOption() && !existingOption.isCorrectOption()) {
            int correctOptionCount = optionRepository.countCorrectOptionsInQuestion(existingOption.getQuestion());
            if(correctOptionCount > 0) {
                throw new BadRequestException(ExamServiceMessages.canNotDuplicateResource("correct options", "question"));
            }
        }

        toOption(existingOption, request);

        return existingOption;
    }

    @Transactional
    public Option delete(UUID uuid) {
        if(uuid == null) {
            throw new BadRequestException(ExamServiceMessages.canNotBeNull("Option.uuid"));
        }

        Option option = get(uuid);
        optionRepository.delete(option);

        return option;
    }

    private Option createOptionFromRequest(OptionRequest request) {
        return new Option()
                .setUuid(UUID.randomUUID())
                .setTitle(request.getTitle())
                .setCorrectOption(request.getCorrectOption());

    }

    private void toOption(Option option, OptionRequest request) {
        if(Util.isNotNullAndEmpty(request.getTitle())) {
            option.setTitle(request.getTitle());
        }

        if(request.getCorrectOption()) {
            option.setCorrectOption(request.getCorrectOption());
        }
    }

}
