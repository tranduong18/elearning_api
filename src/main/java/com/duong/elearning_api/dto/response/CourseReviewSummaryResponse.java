package com.duong.elearning_api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CourseReviewSummaryResponse(
        String courseId,
        Double averageRating,
        long totalReviews,
        List<ReviewResponse> reviews
) {
}
