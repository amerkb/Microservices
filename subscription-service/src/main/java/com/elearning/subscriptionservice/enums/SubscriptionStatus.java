package com.elearning.subscriptionservice.enums;

public enum SubscriptionStatus {
    PENDING,
    ENROLLED,       // تم الاشتراك
    IN_PROGRESS,    // بدأ الدورة
    COMPLETED,      // أنهى الدورة
    PASSED,         // نجح في الاختبار
    FAILED          // فشل في الاختبار
}
