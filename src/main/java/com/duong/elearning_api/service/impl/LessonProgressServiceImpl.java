package com.duong.elearning_api.service.impl;

import com.duong.elearning_api.common.EnrollmentStatus;
import com.duong.elearning_api.dto.request.UpdateProgressRequest;
import com.duong.elearning_api.dto.response.CourseProgressResponse;
import com.duong.elearning_api.entity.Course;
import com.duong.elearning_api.entity.Lesson;
import com.duong.elearning_api.entity.LessonProgress;
import com.duong.elearning_api.entity.User;
import com.duong.elearning_api.exception.AppException;
import com.duong.elearning_api.exception.ErrorCode;
import com.duong.elearning_api.repository.*;
import com.duong.elearning_api.service.LessonProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@Slf4j(topic = "LESSON-PROGRESS-SERVICE")
@RequiredArgsConstructor
public class LessonProgressServiceImpl implements LessonProgressService {
    private final LessonProgressRepository lessonProgressRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public void upsertProgress(String userId, UpdateProgressRequest request) {
        Lesson lesson = lessonRepository.findById(request.lessonId())
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String courseId = lesson.getChapter().getCourse().getId();

        // 1. Áp dụng Upsert pattern: Tìm thấy thì sửa, chưa có thì tạo mới
        LessonProgress progress = lessonProgressRepository.findByUserIdAndLessonId(userId, lesson.getId())
                .orElseGet(() -> LessonProgress.builder()
                        .user(user)
                        .lesson(lesson)
                        .build());
        progress.setWatchedSeconds(request.watchedSeconds());
        // Nếu bài học đã từng hoàn thành trước đó thì luôn giữ trạng thái hoàn thành
        progress.setIsCompleted(progress.getIsCompleted() || request.isCompleted());
        progress.setLastWatchedAt(LocalDateTime.now());

        lessonProgressRepository.save(progress);

        // 2. Tự động kiểm tra nếu hoàn thành 100% thì cập nhật trạng thái Enrollment sang COMPLETED
        long totalLessons = lessonRepository.countByChapter_CourseId(courseId);
        long completedLessons = lessonProgressRepository.countCompletedLessons(userId, courseId);

        if(totalLessons > 0 && completedLessons == totalLessons){
            enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                    .ifPresent(enrollment -> {
                        if(enrollment.getStatus() == EnrollmentStatus.ACTIVE){
                            enrollment.setStatus(EnrollmentStatus.COMPLETED);
                            enrollmentRepository.save(enrollment);
                        }
                    });
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CourseProgressResponse getCourseProgress(String userId, String courseId) {
        // 1. Kiểm tra khóa học tồn tại
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // 2. Đếm tổng số bài học và số bài học đã hoàn thành
        long totalLessons = lessonRepository.countByChapter_CourseId(courseId);
        long completedLessons = lessonProgressRepository.countCompletedLessons(userId, courseId);

        // 3. Tính tỷ lệ % hoàn thành bằng BigDecimal
        BigDecimal percent = BigDecimal.ZERO;
        if(totalLessons > 0){
            percent = BigDecimal.valueOf(completedLessons)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalLessons), 1, RoundingMode.HALF_UP);
        }

        boolean isCompleted = totalLessons > 0 && completedLessons == totalLessons;

        return CourseProgressResponse.builder()
                .courseId(courseId)
                .courseTitle(course.getTitle())
                .totalLessons(totalLessons)
                .completedLessons(completedLessons)
                .percentCompleted(percent)
                .isCourseCompleted(isCompleted)
                .build();
    }
}
