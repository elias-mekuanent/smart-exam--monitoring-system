package com.senpare.examservice.repository;

import com.senpare.examservice.model.Exam;
import com.senpare.examservice.model.ExamSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamSectionRepository extends JpaRepository<ExamSection, UUID> {

    Page<ExamSection> findAllByExam(Exam exam, Pageable pageable);
    List<ExamSection> findAllByExam(Exam exam);
}
