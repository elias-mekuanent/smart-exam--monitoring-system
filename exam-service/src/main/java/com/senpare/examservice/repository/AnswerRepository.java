package com.senpare.examservice.repository;

import com.senpare.examservice.model.Answer;
import com.senpare.examservice.model.ExamSection;
import com.senpare.examservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {

//    @Query("SELECT COUNT(a) FROM Answer a WHERE a.createdBy = ?1 AND a.question = ?2")
    int countByCreatedByAndQuestion(String examineeEmail, Question question);

    Optional<Answer> findFirstByCreatedByAndQuestion(String examineeEmail, Question question);

    @Query("SELECT COUNT(a) FROM Answer a WHERE a.correctAnswer = true AND a.question.examSection = ?1 AND a.createdBy = ?2")
    int countCorrectAnswersByExamSectionAndCreatedBy(ExamSection examSection, String examineeEmail);
}
