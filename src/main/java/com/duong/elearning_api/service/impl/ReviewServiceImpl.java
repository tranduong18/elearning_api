package com.duong.elearning_api.service.impl;

import com.duong.elearning_api.dto.request.CreateReviewRequest;
import com.duong.elearning_api.dto.request.UpdateReviewRequest;
import com.duong.elearning_api.dto.response.CourseReviewSummaryResponse;
import com.duong.elearning_api.dto.response.ReviewResponse;
import com.duong.elearning_api.entity.Course;
import com.duong.elearning_api.entity.Review;
import com.duong.elearning_api.entity.User;
import com.duong.elearning_api.exception.AppException;
import com.duong.elearning_api.exception.ErrorCode;
import com.duong.elearning_api.repository.CourseRepository;
import com.duong.elearning_api.repository.EnrollmentRepository;
import com.duong.elearning_api.repository.ReviewRepository;
import com.duong.elearning_api.repository.UserRepository;
import com.duong.elearning_api.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Slf4j(topic = "REVIEW-SERVICE")
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public ReviewResponse createReview(String userId, CreateReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // 1. Kiểm tra điều kiện tiên quyết: Học viên đã đăng ký khóa học này chưa
        if(!enrollmentRepository.existsByUserIdAndCourseId(userId, request.courseId())){
            throw new AppException(ErrorCode.NOT_ENROLLED);
        }

        // 2. Chống spam: Kiểm tra học viên đã từng đánh giá khóa học này chưa
        if(reviewRepository.existsByUserIdAndCourseId(userId, request.courseId())){
            throw new AppException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        Review review = Review.builder()
                .user(user)
                .course(course)
                .rating(request.rating())
                .comment(request.comment())
                .build();
        reviewRepository.save(review);

        return ReviewResponse.builder()
                .id(review.getId())
                .userId(user.getId())
                .userEmail(user.getEmail())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseReviewSummaryResponse getCourseReviews(String courseId) {
        if(!courseRepository.existsById(courseId)){
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }

        Double rawAvg = reviewRepository.findAverageRatingByCourseId(courseId);
        double roundedAvg = BigDecimal.valueOf(rawAvg).setScale(1, RoundingMode.HALF_UP).doubleValue();
        long total = reviewRepository.countByCourseId(courseId);

        List<ReviewResponse> reviews = reviewRepository.findByCourseIdOrderByCreatedAtDesc(courseId)
                .stream().map(r -> ReviewResponse.builder()
                        .id(r.getId())
                        .userId(r.getUser().getId())
                        .userEmail(r.getUser().getEmail())
                        .rating(r.getRating())
                        .comment(r.getComment())
                        .createdAt(r.getCreatedAt())
                        .build())
                .toList();

        return CourseReviewSummaryResponse.builder()
                .courseId(courseId)
                .averageRating(roundedAvg)
                .totalReviews(total)
                .reviews(reviews)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getMyReview(String userId, String courseId) {
        if(!userRepository.existsById(userId)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if(!courseRepository.existsById(courseId)){
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }

        return reviewRepository.findByUserIdAndCourseId(userId, courseId)
                .map(r -> ReviewResponse.builder()
                        .id(r.getId())
                        .userId(r.getUser().getId())
                        .userEmail(r.getUser().getEmail())
                        .rating(r.getRating())
                        .comment(r.getComment())
                        .createdAt(r.getCreatedAt())
                        .build())
                .orElse(null);
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(String userId, String reviewId, UpdateReviewRequest request) {
        if(!userRepository.existsById(userId)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        if(!review.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.UNAUTHORIZED_REVIEW_ACCESS);
        }

        review.setRating(request.rating());
        review.setComment(request.comment());
        reviewRepository.save(review);

        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .userEmail(review.getUser().getEmail())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public void deleteReview(String userId, String reviewId) {
        if(!userRepository.existsById(userId)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        if(!review.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.UNAUTHORIZED_REVIEW_ACCESS);
        }

        reviewRepository.delete(review);
    }
}
