package com.senpare.examservice.repository;

import com.senpare.examservice.model.Exam;
import com.senpare.examservice.model.ExamEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamEnrollmentRepository extends JpaRepository<ExamEnrollment, UUID> {

    boolean existsByExamAndCreatedBy(Exam exam, String createdBy);

    Optional<ExamEnrollment> findFirstByExamAndCreatedBy(Exam exam, String createdBy);

}
