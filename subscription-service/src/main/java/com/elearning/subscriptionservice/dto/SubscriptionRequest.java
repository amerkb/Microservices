package com.elearning.subscriptionservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {
    private Long courseId;
    private Long userId;  // اختياري، فقط في حالة ADMIN
    private String status; // اختياري
}

