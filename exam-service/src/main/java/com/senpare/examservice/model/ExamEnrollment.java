package com.senpare.examservice.model;

import com.senpare.examservice.model.enumeration.AttendanceStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "exam_enrollment")
public class ExamEnrollment extends AuditableBean {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "exam_uuid", referencedColumnName = "uuid", nullable = false)
    private Exam exam;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false)
    private AttendanceStatus attendanceStatus;

    @Column(nullable = false)
    private String token;

    public ExamEnrollment setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public ExamEnrollment setExam(Exam exam) {
        this.exam = exam;
        return this;
    }

    public ExamEnrollment setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
        return this;
    }

    public ExamEnrollment setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = AttendanceStatus.lookupByCode(attendanceStatus);
        return this;
    }

    public ExamEnrollment setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public ExamEnrollment setCreatedOn(LocalDateTime createdOn) {
        return (ExamEnrollment) super.setCreatedOn(createdOn);
    }

    @Override
    public ExamEnrollment setCreatedBy(String createdBy) {
        return (ExamEnrollment) super.setCreatedBy(createdBy);
    }

    @Override
    public ExamEnrollment setModifiedOn(LocalDateTime modifiedOn) {
        return (ExamEnrollment) super.setModifiedOn(modifiedOn);
    }

    @Override
    public ExamEnrollment setModifiedBy(String modifiedBy) {
        return (ExamEnrollment) super.setModifiedBy(modifiedBy);
    }
}
