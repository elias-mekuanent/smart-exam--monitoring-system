package com.senpare.examservice.controller;

import com.senpare.examservice.dto.QuestionDTO;
import com.senpare.examservice.dto.QuestionRequest;
import com.senpare.examservice.model.ExamSection;
import com.senpare.examservice.model.Option;
import com.senpare.examservice.model.Question;
import com.senpare.examservice.model.enumeration.ExamSectionType;
import com.senpare.examservice.service.AnswerService;
import com.senpare.examservice.service.ExamSectionService;
import com.senpare.examservice.service.OptionService;
import com.senpare.examservice.service.QuestionService;
import com.senpare.examservice.utilities.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/exam-service/questions")
@Tag(name = "Question endpoint", description = "An endpoint to manage questions.")
@RequiredArgsConstructor
public class QuestionController {

    private final ExamSectionService examSectionService;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final AnswerService answerService;

    @PostMapping
    @Operation(summary = "Creates a new question.",
            responses = @ApiResponse(description = "Question successfully created", responseCode = "204",
                    headers = @Header(name = "location", description = "Full path to the newly created question")))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<QuestionDTO> create(@RequestParam("examSectionUuid") String examSectionUuid, @RequestBody QuestionRequest questionRequest, HttpServletRequest request) {
        Question createdQuestion = questionService.create(UUID.fromString(examSectionUuid), questionRequest);
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .path("/" + createdQuestion.getUuid())
                .build()
                .toUri();

        return ResponseEntity.created(location).body(Util.toQuestionDTO(createdQuestion));
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Returns a question associated with the specified UUID.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<QuestionDTO> get(@PathVariable("uuid") String uuid) {
        Question question = questionService.get(UUID.fromString(uuid));
        QuestionDTO questionDTO = Util.toQuestionDTO(question);

        if (question.getExamSection().getExamSectionType() == ExamSectionType.MULTIPLE_CHOICE) {
            List<Option> options = optionService.getAll(question.getUuid());
            questionDTO.setOptions(Util.toOptionDTOs(options, ""));
        }

        return ResponseEntity.ok(questionDTO);
    }

    @GetMapping("/{uuid}/examinee")
    @Operation(summary = "Returns a question associated with the specified UUID. This endpoint is used by examinees while the exam is active (ExamStatus.STARTED)")
    @PreAuthorize("hasAnyRole('EXAMINEE')")
    public ResponseEntity<QuestionDTO> getForExaminee(@PathVariable("uuid") String uuid) {
        Question question = questionService.getForExaminee(UUID.fromString(uuid));
        QuestionDTO questionDTO = Util.toQuestionDTO(question);

        if (question.getExamSection().getExamSectionType() == ExamSectionType.MULTIPLE_CHOICE) {
            String optionUuid = answerService.getAnswerOptionByQuestion(question.getUuid());
            List<Option> options = optionService.getAll(question.getUuid());
            questionDTO.setOptions(Util.toOptionDTOs(options, optionUuid));
        }

        return ResponseEntity.ok(questionDTO);
    }

    @GetMapping
    @Operation(summary = "Returns a paginated list of all question in the specified exam section.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<List<QuestionDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                                    @RequestParam(value = "sort", defaultValue = "createdOn") String sortBy,
                                                    @RequestParam("examSectionUuid") String examSectionUuid) {
        Page<Question> questionPage = questionService.getAll(page, size, sortBy, UUID.fromString(examSectionUuid));
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(questionPage.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(questionPage.getTotalPages()));


        List<QuestionDTO> questionDTOs = Util.toQuestionDTOs(questionPage.getContent());

        ExamSection examSection = examSectionService.get(UUID.fromString(examSectionUuid));
        if (examSection.getExamSectionType() == ExamSectionType.MULTIPLE_CHOICE) {
            questionDTOs.stream()
                    .map(questionDTO -> questionDTO.setOptions(Util.toOptionDTOs(optionService.getAll(questionDTO.getUuid()), "")))
                    .collect(Collectors.toList());
        }

        return new ResponseEntity<>(questionDTOs, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/examinee")
    @Operation(summary = "Returns a paginated list of all question in the specified exam section.")
    @PreAuthorize("hasAnyRole('EXAMINEE')")
    public ResponseEntity<List<QuestionDTO>> getAllForExaminee(@RequestParam(value = "page", defaultValue = "1") int page,
                                                    @RequestParam(value = "sort", defaultValue = "createdOn") String sortBy,
                                                    @RequestParam("examSectionUuid") String examSectionUuid) {
        Page<Question> questionPage = questionService.getAllForExaminee(page, 1, sortBy, UUID.fromString(examSectionUuid));
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(questionPage.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(questionPage.getTotalPages()));


        List<QuestionDTO> questionDTOs = Util.toQuestionDTOs(questionPage.getContent());

        ExamSection examSection = examSectionService.get(UUID.fromString(examSectionUuid));
        if (examSection.getExamSectionType() == ExamSectionType.MULTIPLE_CHOICE) {
            questionDTOs.stream()
                    .map(questionDTO -> {
                        String selectedOption = answerService.getAnswerOptionByQuestion(questionDTO.getUuid());
                        questionDTO.setOptions(Util.toOptionDTOs(optionService.getAll(questionDTO.getUuid()), selectedOption));
                        return questionDTO;
                    })
                    .collect(Collectors.toList());
        }

        return new ResponseEntity<>(questionDTOs, responseHeaders, HttpStatus.OK);
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Updates an existing question.",
            responses = @ApiResponse(description = "Question successfully updated", responseCode = "200"))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<QuestionDTO> update(@PathVariable("uuid") String uuid, @RequestBody QuestionRequest questionRequest) {
        Question updatedQuestion = questionService.update(UUID.fromString(uuid), questionRequest);
        return ResponseEntity.ok(Util.toQuestionDTO(updatedQuestion));
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete question associated with the specified UUID.",
            responses = @ApiResponse(description = "Question successfully deleted", responseCode = "200"))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<Void> delete(@PathVariable("uuid") String uuid) {
        questionService.delete(UUID.fromString(uuid));
        return ResponseEntity.noContent().build();
    }

}
