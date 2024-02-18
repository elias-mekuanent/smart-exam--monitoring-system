package com.senpare.examservice.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "answer")
public class Answer extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @OneToOne(optional = false, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "question_uuid", referencedColumnName = "uuid")
    private Question question;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "option_uuid", referencedColumnName = "uuid")
    private Option option;

    @Column(name = "answer_text", columnDefinition = "TEXT")
    private String answerText;

    private int attempt;

    @Column(name = "time_taken")
    private Time timeTaken;

    @Column(name = "correct_answer", nullable = false)
    private boolean correctAnswer;

    public Answer setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Answer setQuestion(Question question) {
        this.question = question;
        return this;
    }

    public Answer setOption(Option option) {
        this.option = option;
        return this;
    }

    public Answer setAnswerText(String answerText) {
        this.answerText = answerText;
        return this;
    }

    public Answer setAttempt(int attempt) {
        this.attempt = attempt;
        return this;
    }

    public Answer setTimeTaken(Time timeTaken) {
        this.timeTaken = timeTaken;
        return this;
    }

    public Answer setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
        return this;
    }

    @Override
    public Answer setCreatedOn(LocalDateTime createdOn) {
        return (Answer) super.setCreatedOn(createdOn);
    }

    @Override
    public Answer setCreatedBy(String createdBy) {
        return (Answer) super.setCreatedBy(createdBy);
    }

    @Override
    public Answer setModifiedOn(LocalDateTime modifiedOn) {
        return (Answer) super.setModifiedOn(modifiedOn);
    }

    @Override
    public Answer setModifiedBy(String modifiedBy) {
        return (Answer) super.setModifiedBy(modifiedBy);
    }
}
