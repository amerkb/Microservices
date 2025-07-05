package com.elearning.examservice.service;

import com.elearning.examservice.client.UserClient;
import com.elearning.examservice.dto.ExamSubmissionRequest;
import com.elearning.examservice.model.Question;
import com.elearning.examservice.model.Submission;
import com.elearning.examservice.model.UserAnswer;
import com.elearning.examservice.repository.QuestionRepository;
import com.elearning.examservice.repository.SubmissionRepository;
import com.elearning.examservice.repository.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final QuestionRepository questionRepo;
    private final UserAnswerRepository userAnswerRepo;
    private final SubmissionRepository submissionRepo;
    private final WebClient.Builder webClientBuilder;
    private final UserClient userClient;

    public boolean submitExam(ExamSubmissionRequest request, String token) {
        Long userId = userClient.getUserId(token).join();
        int total = request.getAnswers().size();
        int correct = 0;

        for (var ans : request.getAnswers()) {
            Question q = questionRepo.findById(ans.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            boolean isCorrect = q.getCorrectAnswer().equalsIgnoreCase(ans.getAnswer());
            if (isCorrect) correct++;

            UserAnswer userAnswer = UserAnswer.builder()
                    .userId(userId)
                    .courseId(request.getCourseId())
                    .questionId(q.getId())
                    .answer(ans.getAnswer())
                    .correct(isCorrect)
                    .build();

            userAnswerRepo.save(userAnswer);        }

        boolean passed = correct >= (total * 0.6);
        int score = (correct * 100) / total;

        // حفظ النتيجة
        Submission submission = Submission.builder()
                .userId(userId)
                .courseId(request.getCourseId())
                .score(score)
                .passed(passed)
                .build();
        submissionRepo.save(submission);

        // إرسال الحالة إلى خدمة الاشتراك
        updateSubscriptionStatus(userId, request.getCourseId(), passed);

        return passed;
    }

    private void updateSubscriptionStatus(Long userId, Long courseId, boolean passed) {
        String status = passed ? "COMPLETED" : "FAILED";

        webClientBuilder.build()
                .put()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8080)
                        .path("/subscription-service/subscriptions/update-status")
                        .queryParam("userId", userId)
                        .queryParam("courseId", courseId)
                        .queryParam("status", status)
                        .build())
                .retrieve()
                .toBodilessEntity()
                .block();

    }
}
