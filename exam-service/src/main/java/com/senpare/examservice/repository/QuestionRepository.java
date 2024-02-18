package com.senpare.examservice.repository;

import com.senpare.examservice.model.ExamSection;
import com.senpare.examservice.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID>  {

//    @Query(value = "SELECT q FROM Question q WHERE q.examSection.uuid = ?1")
    Page<Question> findAllByExamSection(Pageable pageable, ExamSection examSection);
}
