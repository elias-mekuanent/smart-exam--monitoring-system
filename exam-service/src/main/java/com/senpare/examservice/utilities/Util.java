package com.senpare.examservice.utilities;

import com.senpare.examservice.dto.*;
import com.senpare.examservice.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Util {

    public static <T, R extends JpaRepository> Page<T> paginate(R repository, int page, int size, String sortBy) {
        Pageable pageRequest = getPageable(page, size, sortBy);

        return repository.findAll(pageRequest == null ? Pageable.unpaged() : pageRequest);
    }

    public static Pageable getPageable(int page, int size, String sortBy) {
        int actualPage = page - 1;
        Pageable pageRequest;
        if(sortBy != null && !sortBy.isEmpty()) {
            if(sortBy.startsWith("-")) {
                sortBy = sortBy.substring(1);
                pageRequest =  PageRequest.of(actualPage, size, Sort.by(Sort.Direction.DESC, sortBy));
            } else {
                pageRequest =  PageRequest.of(actualPage, size, Sort.by(Sort.Direction.ASC, sortBy));
            }
        } else {
            pageRequest =  PageRequest.of(actualPage, size);
        }
        return pageRequest;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotNullAndEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean isNotNullAndEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static String toHumanReadableFormat(LocalDateTime localDateTime) {
        if(localDateTime == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM uuuu hh:mm:ss a");
        return localDateTime.format(formatter);
    }

    public static String toHumanReadableFormat(Duration duration) {
        if(duration == null) {
            return null;
        }

        int hoursPart = duration.toHoursPart();
        int minutesPart = duration.toMinutesPart();

        if(hoursPart == 0) {
            return String.format("%d minutes", minutesPart);
        }

        return String.format("%d hours, %d minutes", hoursPart, minutesPart);
    }

    public static ExamDTO toExamDTO(Exam exam) {
        ExamDTO examDTO = new ExamDTO();

        if(exam == null) {
            return examDTO;
        }

        return examDTO
                .setUuid(exam.getUuid())
                .setTitle(exam.getTitle())
                .setDescription(exam.getDescription())
                .setExamInstruction(exam.getExamInstruction())
                .setExamType(exam.getExamType())
                .setExamStatus(exam.getExamStatus())
                .setLicenseUuid(exam.getLicenseUuid())
                .setLicenseUuid(exam.getLicenseUuid())
                .setPlannedStartDateTime(exam.getPlannedStartDateTime())
                .setActualStartDateTime(exam.getActualStartDateTime())
                .setPlannedDuration(exam.getPlannedDuration())
                .setActualDuration(exam.getActualDuration())
                .setExamSettingDTO(toExamSettingDTO(exam.getExamSetting()))
                .setCreatedOn(exam.getCreatedOn())
                .setModifiedOn(exam.getModifiedOn())
                .setCreatedBy(exam.getCreatedBy())
                .setModifiedBy(exam.getModifiedBy());
    }

    public static List<ExamDTO> toExamDTOs(List<Exam> exams) {
        if(Util.isNullOrEmpty(exams)) {
            Collections.emptyList();
        }

        return exams.stream()
                .map(Util::toExamDTO)
                .toList();
    }

    public static ExamDetail toExamDetail(Exam exam) {
        ExamDetail examDetail = new ExamDetail();

        if(exam == null) {
            return examDetail;
        }

        return examDetail
                .setTitle(exam.getTitle())
                .setDescription(exam.getDescription())
                .setExamInstruction(exam.getExamInstruction())
                .setExamType(exam.getExamType())
                .setPlannedStartDateTime(exam.getPlannedStartDateTime())
                .setPlannedDuration(exam.getPlannedDuration());
    }

    public static ExamSettingDTO toExamSettingDTO(ExamSetting examSetting) {
        ExamSettingDTO examSettingDTO = new ExamSettingDTO();

        if(examSetting == null) {
            return examSettingDTO;
        }


        return examSettingDTO
                .setUuid(examSetting.getUuid())
                .setIncludeMockExam(examSetting.isIncludeMockExam())
                .setIncludeCameraSecurity(examSetting.isIncludeCameraSecurity())
                .setIncludeVoiceRecordSecurity(examSetting.isIncludeVoiceRecordSecurity())
                .setIncludeMouseTrackSecurity(examSetting.isIncludeMouseTrackSecurity())
                .setIncludeScreenSecurity(examSettingDTO.isIncludeScreenSecurity())
                .setMaxSecurityStrike(examSetting.getMaxSecurityStrike())
                .setAutoGrade(examSetting.isAutoGrade())
                .setShuffleQuestion(examSetting.isShuffleQuestion())
                .setExamineeCount(examSetting.getExamineeCount())
                ;

    }

    public static ExamSectionDTO toExamSectionDTO(ExamSection examSection) {
        ExamSectionDTO examSectionDTO = new ExamSectionDTO();

        if (examSection == null) {
            return examSectionDTO;
        }

        return examSectionDTO
                .setUuid(examSection.getUuid())
                .setExamUuid(examSection.getExam() != null ? examSection.getExam().getUuid() : null)
                .setTitle(examSection.getTitle())
                .setExamSectionType(examSection.getExamSectionType())
                .setWeightPerQuestion(examSection.getWeightPerQuestion())
                .setCreatedOn(examSection.getCreatedOn())
                .setModifiedOn(examSection.getModifiedOn())
                .setCreatedBy(examSection.getCreatedBy())
                .setModifiedBy(examSection.getModifiedBy());
    }

    public static List<ExamSectionDTO> toExamSectionDTOs(List<ExamSection> examSections) {
        if(Util.isNullOrEmpty(examSections)) {
            Collections.emptyList();
        }

        return examSections.stream()
                .map(Util::toExamSectionDTO)
                .toList();
    }

    public static QuestionDTO toQuestionDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();

        if (question == null) {
            return questionDTO;
        }

        return new QuestionDTO()
                .setUuid(question.getUuid())
                .setExamSectionUuid(question.getExamSection() != null ? question.getExamSection().getUuid() : null)
                .setTitle(question.getTitle())
                .setOptionOrderType(question.getOptionOrderType())
                .setCreatedOn(question.getCreatedOn())
                .setModifiedOn(question.getModifiedOn())
                .setCreatedBy(question.getCreatedBy())
                .setModifiedBy(question.getModifiedBy());
    }

    public static List<QuestionDTO> toQuestionDTOs(List<Question> questions) {
        if(Util.isNullOrEmpty(questions)) {
            Collections.emptyList();
        }

        return questions.stream()
                .map(Util::toQuestionDTO)
                .toList();
    }

    public static OptionDTO toOptionDTO(Option option) {
        OptionDTO optionDTO = new OptionDTO();

        if (option == null) {
            return optionDTO;
        }

        return new OptionDTO()
                .setUuid(option.getUuid())
                .setQuestionUuid(option.getQuestion() != null ? option.getQuestion().getUuid() : null)
                .setTitle(option.getTitle())
                .setCreatedOn(option.getCreatedOn())
                .setModifiedOn(option.getModifiedOn())
                .setCreatedBy(option.getCreatedBy())
                .setModifiedBy(option.getModifiedBy());
    }

    public static List<OptionDTO> toOptionDTOs(List<Option> options, String selectedOptionUuid) {
        if(Util.isNullOrEmpty(options)) {
            Collections.emptyList();
        }

        return options.stream()
                .map(Util::toOptionDTO)
                .map(optionDTO -> {
                    if(optionDTO.getUuid().toString().equals(selectedOptionUuid)) {
                        optionDTO.setSelected(true);
                        log.debug("Already answered option found");
                    }
                    return optionDTO;
                })
                .toList();
    }


    public static ExamSecurityStrikeDTO toExamSecurityStrikeDTO(ExamSecurityStrike examSecurityStrike) {
        ExamSecurityStrikeDTO examSecurityStrikeDTO = new ExamSecurityStrikeDTO()
                .setStrikeType(examSecurityStrike.getStrikeType())
                .setExamTime(toHumanReadableFormat(examSecurityStrike.getExamTime()))
                .setExamineeEmail(examSecurityStrike.getCreatedBy())
                ;

        return examSecurityStrikeDTO;
    }

    public static List<ExamSecurityStrikeDTO> toExamSecurityStrikeDTOs(List<ExamSecurityStrike> examSecurityStrikes) {
        List<ExamSecurityStrikeDTO> examSecurityStrikeDTOs = new ArrayList<>();

        return examSecurityStrikes.stream()
                .map(examSecurityStrike -> toExamSecurityStrikeDTO(examSecurityStrike))
                .collect(Collectors.toList());

    }
}
