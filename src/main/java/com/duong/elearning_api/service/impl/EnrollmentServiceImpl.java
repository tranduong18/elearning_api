package com.duong.elearning_api.service.impl;

import com.duong.elearning_api.common.CourseStatus;
import com.duong.elearning_api.common.EnrollmentStatus;
import com.duong.elearning_api.dto.request.EnrollCourseRequest;
import com.duong.elearning_api.dto.response.EnrollmentResponse;
import com.duong.elearning_api.entity.Course;
import com.duong.elearning_api.entity.Enrollment;
import com.duong.elearning_api.entity.User;
import com.duong.elearning_api.exception.AppException;
import com.duong.elearning_api.exception.ErrorCode;
import com.duong.elearning_api.repository.CourseRepository;
import com.duong.elearning_api.repository.EnrollmentRepository;
import com.duong.elearning_api.repository.UserRepository;
import com.duong.elearning_api.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j(topic = "ENROLLMENT-SERVICE")
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public EnrollmentResponse enrollCourse(String userId, EnrollCourseRequest request) {
        // 1. Kiểm tra thông tin người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 2. Kiểm tra thông tin khóa học
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // 3. Khóa học phải ở trạng thái PUBLISHED mới được phép đăng ký
        if(course.getStatus() != CourseStatus.PUBLISHED){
            throw new AppException(ErrorCode.COURSE_NOT_PUBLISHED);
        }

        // 4. Chống mua trùng lặp: Kiểm tra học viên đã đăng ký khóa học này chưa
        if(enrollmentRepository.existsByUserIdAndCourseId(userId, course.getId())){
            throw new AppException(ErrorCode.COURSE_ALREADY_ENROLLED);
        }

        // 5. Tạo bản ghi đăng ký với giá chốt thực tế (Financial Snapshot)
        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .pricePaid(course.getPrice())
                .status(EnrollmentStatus.ACTIVE)
                .build();
        enrollmentRepository.save(enrollment);

        return toEnrollmentResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getMyEnrollments(String userId) {
        return enrollmentRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toEnrollmentResponse)
                .toList();
    }

    private EnrollmentResponse toEnrollmentResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .userId(enrollment.getUser().getId())
                .userEmail(enrollment.getUser().getEmail())
                .courseId(enrollment.getCourse().getId())
                .courseTitle(enrollment.getCourse().getTitle())
                .pricePaid(enrollment.getPricePaid())
                .status(enrollment.getStatus())
                .enrolledAt(enrollment.getCreatedAt())
                .build();
    }
}
