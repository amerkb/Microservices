package com.elearning.subscriptionservice.model;

import com.elearning.subscriptionservice.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Subscription {
    @Id @GeneratedValue
    private Long id;

    private Long userId;
    private Long courseId;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status = SubscriptionStatus.ENROLLED;

}
