package com.senpare.examservice.repository;

import com.senpare.examservice.model.Exam;
import com.senpare.examservice.model.enumeration.ExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExamRepository extends JpaRepository<Exam, UUID> {

    int countByExamStatus(ExamStatus status);


    Page<Exam> findAllByCreatedBy(String examinedEmail, Pageable pageable);

}
