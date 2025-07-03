package com.elearning.examservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class CourseClient {

    private final WebClient.Builder webClientBuilder;

    public boolean isTrainerOwnerOfCourse(Long courseId, Long trainerId) {
        return webClientBuilder.build()
                .get()
                .uri("http://course-service/courses/" + courseId + "/is-owner?trainerId=" + trainerId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
    public boolean isUserEnrolledInCourse(Long courseId, Long userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://course-service/courses/" + courseId + "/is-enrolled?userId=" + userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }


}
