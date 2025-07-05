package com.elearning.courseservice.controller;

import com.elearning.courseservice.client.UserClient;
import com.elearning.courseservice.model.Course;
import com.elearning.courseservice.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;
    private final WebClient.Builder webClientBuilder;
    private final UserClient userClient;



    @PostMapping
    public ResponseEntity<Course> create(
            @RequestBody @Valid Course course,
            @RequestHeader("Authorization") String token
    ) {
        Course created = service.create(course, token);
        return ResponseEntity.ok(created);
    }


    @PutMapping("/{id}/approve")
    public ResponseEntity<Course> approve(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        Course approved = service.approve(id, token);
        return ResponseEntity.ok(approved);
    }

    @GetMapping
    public ResponseEntity<List<Course>> list(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        List<Course> courses = service.list(token);
        return ResponseEntity.ok(courses);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Course> getById(@PathVariable Long id) {
        Course course = service.getById(id);
        return ResponseEntity.ok(course);
    }
    @GetMapping("/check")
    public String testLoadBalancing(  @RequestHeader("Authorization") String token) {
//        return String role = userClient.getRole(token).join();
        return webClientBuilder.build()
                .get()
                .uri("http://user-service/users/instance")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


}
