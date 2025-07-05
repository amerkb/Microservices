package com.elearning.subscriptionservice.repository;

import com.elearning.subscriptionservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(Long userId);
    Optional<Subscription> findByUserIdAndCourseId(Long userId, Long courseId);
}
