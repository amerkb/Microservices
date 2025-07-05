package com.elearning.examservice.repository;

import com.elearning.examservice.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findByUserIdAndCourseId(Long userId, Long courseId);
}
