package com.senpare.examservice.repository;

import com.senpare.examservice.model.ExamSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamSettingRepository extends JpaRepository<ExamSetting, UUID> {

    Optional<ExamSetting> findByExam(UUID examUuid);
}
