package com.duong.elearning_api.controller;

import com.duong.elearning_api.dto.request.CreateReviewRequest;
import com.duong.elearning_api.dto.request.UpdateReviewRequest;
import com.duong.elearning_api.dto.response.ApiResponse;
import com.duong.elearning_api.dto.response.CourseReviewSummaryResponse;
import com.duong.elearning_api.dto.response.ReviewResponse;
import com.duong.elearning_api.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // 1. Gửi đánh giá khóa học (Bắt buộc phải đăng nhập và đã đăng ký học)
    @PostMapping("/reviews")
    public ApiResponse<ReviewResponse> createReview(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateReviewRequest request
    ){
        String userId = jwt.getSubject();
        ReviewResponse data = reviewService.createReview(userId, request);

        return ApiResponse.<ReviewResponse>builder()
                .code(200)
                .message("Create review successfully")
                .data(data)
                .build();
    }

    // 2. Xem danh sách đánh giá và điểm trung bình (Công khai cho mọi người xem)
    @GetMapping("/course/{courseId}/reviews")
    public ApiResponse<CourseReviewSummaryResponse> getCourseReviews(@PathVariable String courseId){
        CourseReviewSummaryResponse data = reviewService.getCourseReviews(courseId);
        return ApiResponse.<CourseReviewSummaryResponse>builder()
                .code(200)
                .message("Get course reviews successfully")
                .data(data)
                .build();
    }

    // 3. Xem đánh giá của tôi trên một khóa học
    @GetMapping("/courses/{courseId}/my-review")
    public ApiResponse<ReviewResponse> getMyReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String courseId
    ) {
        String userId = jwt.getSubject();
        ReviewResponse data = reviewService.getMyReview(userId, courseId);
        return ApiResponse.<ReviewResponse>builder()
                .code(200)
                .message("Get my review successfully")
                .data(data)
                .build();
    }

    // 4. Cập nhật đánh giá của tôi
    @PutMapping("/reviews/{reviewId}")
    public ApiResponse<ReviewResponse> updateReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String reviewId,
            @Valid @RequestBody UpdateReviewRequest request
    ) {
        String userId = jwt.getSubject();
        ReviewResponse data = reviewService.updateReview(userId, reviewId, request);
        return ApiResponse.<ReviewResponse>builder()
                .code(200)
                .message("Update review successfully")
                .data(data)
                .build();
    }

    // 5. Xóa đánh giá của tôi
    @DeleteMapping("/reviews/{reviewId}")
    public ApiResponse<Void> deleteReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String reviewId
    ) {
        String userId = jwt.getSubject();
        reviewService.deleteReview(userId, reviewId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete review successfully")
                .build();
    }
}
