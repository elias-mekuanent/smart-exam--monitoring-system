package com.senpare.examservice.repository;

import com.senpare.examservice.model.Exam;
import com.senpare.examservice.model.ExamSecurityStrike;
import com.senpare.examservice.model.enumeration.SecurityStrikeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamSecurityStrikeRepository extends JpaRepository<ExamSecurityStrike, UUID> {

    @Query("SELECT COUNT(ess) FROM ExamSecurityStrike ess WHERE ess.exam = ?1 AND ess.strikeType = ?2 AND ess.createdBy = ?3")
    int countByExamAndStrikeTypeAndCreatedBy(Exam exam, SecurityStrikeType strikeType, String examineeEmal);

    List<ExamSecurityStrike> findAllByExamAndCreatedBy(Exam exam, String examineeEmail);
}
