package com.elearning.subscriptionservice.controller;

import com.elearning.subscriptionservice.dto.SubscriptionRequest;
import com.elearning.subscriptionservice.enums.SubscriptionStatus;
import com.elearning.subscriptionservice.model.Subscription;
import com.elearning.subscriptionservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @PostMapping
    public ResponseEntity<?> subscribe(@RequestBody SubscriptionRequest request,
                                       @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.subscribe(request, token));
    }


    @GetMapping("/my")
    public ResponseEntity<List<Subscription>> myCourses(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.myCourses(token));
    }

    @PutMapping("/update-status")
    public ResponseEntity<Void> updateStatusByUserAndCourse(@RequestParam Long userId,
                                                            @RequestParam Long courseId,
                                                            @RequestParam SubscriptionStatus status) {
        service.updateStatusByUserAndCourse(userId, courseId, status);
        return ResponseEntity.ok().build();
    }


}
