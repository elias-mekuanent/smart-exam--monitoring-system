package com.senpare.examservice.controller;

import com.senpare.examservice.dto.OptionDTO;
import com.senpare.examservice.dto.OptionRequest;
import com.senpare.examservice.model.Option;
import com.senpare.examservice.service.OptionService;
import com.senpare.examservice.utilities.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exam-service/options")
@Tag(name = "Option endpoint", description = "An endpoint to manage options.")
@RequiredArgsConstructor
public class OptionController {

    private final OptionService optionService;

    @PostMapping
    @Operation(summary = "Creates a new option.",
            responses = @ApiResponse(description = "Option successfully created", responseCode = "204",
                    headers = @Header(name = "location", description = "Full path to the newly created option")))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<OptionDTO> create(@RequestParam("questionUuid") String questionUuid, @RequestBody OptionRequest optionRequest, HttpServletRequest request) {
        Option createdOption = optionService.create(UUID.fromString(questionUuid), optionRequest);
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .path("/" + createdOption.getUuid())
                .build()
                .toUri();

        return ResponseEntity.created(location).body(Util.toOptionDTO(createdOption));
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Returns an option associated with the specified UUID.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<OptionDTO> get(@PathVariable("uuid") String uuid) {
        Option option = optionService.get(UUID.fromString(uuid));
        return ResponseEntity.ok(Util.toOptionDTO(option));
    }

    @GetMapping
    @Operation(summary = "Returns a paginated list of all options in the specified question.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<List<OptionDTO>> getAll(@RequestParam("questionUuid") String questionUuid) {
        List<Option> options = optionService.getAll(UUID.fromString(questionUuid));
        return ResponseEntity.ok(Util.toOptionDTOs(options, null));
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Updates an existing option.",
            responses = @ApiResponse(description = "Option successfully updated", responseCode = "200"))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<OptionDTO> update(@PathVariable("uuid") String uuid, @RequestBody OptionRequest optionRequest) {
        Option updatedOption = optionService.update(UUID.fromString(uuid), optionRequest);
        return ResponseEntity.ok(Util.toOptionDTO(updatedOption));
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete option associated with the specified UUID.",
            responses = @ApiResponse(description = "Option successfully deleted", responseCode = "200"))
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<Void> delete(@PathVariable("uuid") String uuid) {
        optionService.delete(UUID.fromString(uuid));
        return ResponseEntity.noContent().build();
    }
}
