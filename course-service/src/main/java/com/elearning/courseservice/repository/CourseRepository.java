package com.elearning.courseservice.repository;

import com.elearning.courseservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // جلب جميع الدورات المعتمدة فقط (للمتعلمين)
    List<Course> findByIsApprovedTrue();

    // جلب الدورات الخاصة بمدرب معين
    List<Course> findByTrainerId(Long trainerId);
}
