package com.duong.elearning_api.service;

import com.duong.elearning_api.dto.request.CreateReviewRequest;
import com.duong.elearning_api.dto.request.UpdateReviewRequest;
import com.duong.elearning_api.dto.response.CourseReviewSummaryResponse;
import com.duong.elearning_api.dto.response.ReviewResponse;

public interface ReviewService {
    ReviewResponse createReview(String userId, CreateReviewRequest request);

    CourseReviewSummaryResponse getCourseReviews(String courseId);

    ReviewResponse getMyReview(String userId, String courseId);

    ReviewResponse updateReview(String userId, String reviewId, UpdateReviewRequest request);

    void deleteReview(String userId, String reviewId);
}
