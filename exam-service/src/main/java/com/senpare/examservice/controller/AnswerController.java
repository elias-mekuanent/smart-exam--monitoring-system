package com.senpare.examservice.controller;

import com.senpare.examservice.dto.AnswerDTO;
import com.senpare.examservice.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exam-service/answers")
@Tag(name = "Answer endpoint", description = "An endpoint to manage answers.")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    @Operation(summary = "Creates answer for the specified question or updates it if it already exists.",
            responses = @ApiResponse(description = "Answer successfully created/updated", responseCode = "200"))
    @PreAuthorize("hasAnyRole('EXAMINEE')")
    public ResponseEntity<Void> createAndUpdate(@RequestBody AnswerDTO answerDTO) {
        answerService.createAndUpdate(answerDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{questionUUid}/selected-answer")
    @Operation(summary = "Returns previously selected answer for the specified question. If there is no previous answer it returns null without throwing any exception",
            responses = @ApiResponse(description = "Answer successfully created/updated", responseCode = "200"))
    @PreAuthorize("hasAnyRole('EXAMINEE')")
    public ResponseEntity<String> getSelectedAnswer(@PathVariable("questionUUid") String questionUuid) {
        String  optionUuid = answerService.getAnswerOptionByQuestion(UUID.fromString(questionUuid));
        return ResponseEntity.ok(optionUuid);
    }

}
