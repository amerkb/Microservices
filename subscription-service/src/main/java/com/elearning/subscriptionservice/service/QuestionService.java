package com.elearning.examservice.service;

import com.elearning.examservice.client.CourseClient;
import com.elearning.examservice.client.UserClient;
import com.elearning.examservice.dto.CreateQuestionRequest;
import com.elearning.examservice.model.Question;
import com.elearning.examservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository repo;
    private final UserClient userClient;
    private final CourseClient courseClient;

    public Question create(CreateQuestionRequest request, String token) {
        String role = userClient.getRole(token);
        Long trainerId = userClient.getUserId(token);

        if (!"TRAINER".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only trainers can add questions");
        }

//        if (!courseClient.isTrainerOwnerOfCourse(request.getCourseId(), trainerId)) {
//            throw new RuntimeException("Trainer does not own this course");
//        }

        Question q = Question.builder()
                .courseId(request.getCourseId())
                .content(request.getContent())
                .correctAnswer(request.getCorrectAnswer())
                .build();

        return repo.save(q);
    }

    public List<Question> getByCourse(Long courseId, String token) {
//        if (!courseClient.isUserEnrolledInCourse(courseId, userClient.getUserId(token))) {
//            throw new RuntimeException("User not enrolled in this course");
//        }
        return repo.findByCourseId(courseId);
    }
}