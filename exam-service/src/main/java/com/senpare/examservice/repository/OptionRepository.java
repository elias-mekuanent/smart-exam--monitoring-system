package com.senpare.examservice.repository;

import com.senpare.examservice.model.Option;
import com.senpare.examservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OptionRepository extends JpaRepository<Option, UUID> {

    List<Option> findAllByQuestion(Question question);

    @Query("SELECT COUNT(o) FROM Option o WHERE o.question = ?1 AND o.correctOption = true")
    int countCorrectOptionsInQuestion(Question question);
}
