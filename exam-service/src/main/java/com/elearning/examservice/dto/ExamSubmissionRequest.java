package com.elearning.examservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExamSubmissionRequest {
    private Long courseId;
    private List<AnswerDto> answers;

    @Data
    public static class AnswerDto {
        private Long questionId;
        private String answer;
    }
}
