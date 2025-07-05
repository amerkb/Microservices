package com.elearning.courseservice.service;

import com.elearning.courseservice.client.UserClient;
import com.elearning.courseservice.model.Course;
import com.elearning.courseservice.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository repo;
    private final UserClient userClient;


    @Transactional
    public Course create(Course course, String token) {
        String role = userClient.getRole(token).join();
        Long trainerId = userClient.getUserId(token).join();

        if (!"TRAINER".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only trainers can create courses");
        }

        course.setTrainerId(trainerId);
        course.setIsApproved(false); // Requires admin approval
        return repo.save(course);
    }

    @Transactional
    public Course approve(Long courseId, String token) {
        String role = userClient.getRole(token).join();

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only admins can approve courses");
        }

        Course course = repo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setIsApproved(true);
        return repo.save(course);
    }


    public List<Course> list(String token) {
        String role = userClient.getRole(token).join();
        Long trainerId = userClient.getUserId(token).join();

        if ("ADMIN".equalsIgnoreCase(role)) {
            return repo.findAll();
        } else if ("TRAINER".equalsIgnoreCase(role)) {
            return repo.findByTrainerId(trainerId);
        } else {
            return repo.findByIsApprovedTrue();
        }
    }

    public Course getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

}
