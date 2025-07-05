package com.elearning.subscriptionservice.service;
import com.elearning.subscriptionservice.client.CourseClient;
import com.elearning.subscriptionservice.client.UserClient;
import com.elearning.subscriptionservice.dto.SubscriptionRequest;
import com.elearning.subscriptionservice.enums.SubscriptionStatus;
import com.elearning.subscriptionservice.model.Subscription;
import com.elearning.subscriptionservice.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository repo;
    private final UserClient userClient;
    private final CourseClient courseClient;

    public Subscription subscribe(SubscriptionRequest request, String token) {
        String role = userClient.getRole(token).join();

        Long userId;

        if ("ADMIN".equals(role) || "TRAINER".equals(role)) {
            if (request.getUserId() == null || request.getStatus() == null) {
                throw new RuntimeException("Admin must provide userId and status");
            }
            userId = request.getUserId();
        } else {
            // استخدم userId من التوكن
            userId = userClient.getUserId(token).join();
        }

        Long courseId = request.getCourseId();

        // تحقق من وجود الدورة
//        courseClient.getCourse(courseId);

        // تحقق من عدم الاشتراك مسبقًا
        if (repo.findByUserIdAndCourseId(userId, courseId).isPresent()) {
            throw new RuntimeException("Already subscribed to this course");
        }

        // إنشاء الاشتراك
        Subscription subscription = new Subscription();
        subscription.setCourseId(courseId);
        subscription.setUserId(userId);
        subscription.setStatus(SubscriptionStatus.valueOf("ADMIN".equals(role) ? request.getStatus() : "PENDING"));

        return repo.save(subscription);
    }

    public List<Subscription> myCourses(String token) {
        Long userId = userClient.getUserId(token).join();
        return repo.findByUserId(userId);
    }
    public void updateStatusByUserAndCourse(Long userId, Long courseId, SubscriptionStatus status) {
        Subscription subscription = repo.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscription.setStatus(status);
        repo.save(subscription);
    }


}
