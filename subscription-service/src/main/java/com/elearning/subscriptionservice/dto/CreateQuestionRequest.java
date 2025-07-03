package com.elearning.examservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateQuestionRequest {

    @NotNull
    private Long courseId;

    @NotBlank
    private String content;

    @NotBlank
    private String correctAnswer;
}
