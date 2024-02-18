package com.senpare.authservice.client;

import com.senpare.authservice.client.response.Exam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("exam-service")
public interface ExamClient {

    @GetMapping("/api/v1/exam-service/exams/{uuid}")
    Exam getExam(@RequestParam("uuid") String uuid);
}
