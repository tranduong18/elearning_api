package com.duong.elearning_api.dto.response;

import com.duong.elearning_api.common.EnrollmentStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record EnrollmentResponse(
        String id,
        String userId,
        String userEmail,
        String courseId,
        String courseTitle,
        BigDecimal pricePaid,
        EnrollmentStatus status,
        LocalDateTime enrolledAt
) {
}
