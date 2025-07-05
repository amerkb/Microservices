package com.elearning.examservice.controller;

import com.elearning.examservice.dto.CreateQuestionRequest;
import com.elearning.examservice.dto.ExamSubmissionRequest;
import com.elearning.examservice.model.Question;
import com.elearning.examservice.service.ExamService;
import com.elearning.examservice.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService service;
    private final ExamService examService;
    @PostMapping
    public ResponseEntity<Question> create(@RequestBody @Valid CreateQuestionRequest request,
                                           @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.create(request, token));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Question>> byCourse(@PathVariable Long courseId,
                                                   @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.getByCourse(courseId, token));
    }
    @PostMapping("/submit")
    public ResponseEntity<?> submitAnswers(@RequestBody ExamSubmissionRequest request,
                                           @RequestHeader("Authorization") String token) {
        boolean passed = examService.submitExam(request, token);
        return ResponseEntity.ok(Map.of("passed", passed));
    }

}