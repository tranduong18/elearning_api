package com.duong.elearning_api.service.impl;

import com.duong.elearning_api.common.CourseStatus;
import com.duong.elearning_api.dto.request.CreateCourseRequest;
import com.duong.elearning_api.dto.response.CourseDetailResponse;
import com.duong.elearning_api.entity.Course;
import com.duong.elearning_api.exception.AppException;
import com.duong.elearning_api.exception.ErrorCode;
import com.duong.elearning_api.repository.CourseRepository;
import com.duong.elearning_api.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "COURSE-SERVICE")
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    @Override
    public CourseDetailResponse createCourse(CreateCourseRequest request) {
        if(courseRepository.existsByTitleIgnoreCase(request.title())){
            throw new AppException(ErrorCode.COURSE_TITLE_DUPLICATE);
        }

        Course course = Course.builder()
                .title(request.title())
                .description(request.description())
                .price(request.price())
                .level(request.level())
                .status(CourseStatus.DRAFT)
                .isActive(true)
                .build();

        courseRepository.save(course);
        return mapToDetailResponse(course);
    }

    @Override
    public CourseDetailResponse updateCourse(String id, Course course) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        existingCourse.setTitle(course.getTitle());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setPrice(course.getPrice());
        existingCourse.setLevel(course.getLevel());
        existingCourse.setStatus(course.getStatus());

        courseRepository.save(existingCourse);
        return mapToDetailResponse(existingCourse);
    }

    @Override
    public CourseDetailResponse getCourseById(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        return mapToDetailResponse(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public void deleteCourse(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        course.setIsActive(false);
        courseRepository.save(course);
    }

    private CourseDetailResponse mapToDetailResponse(Course course) {
        return CourseDetailResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .level(course.getLevel())
                .status(course.getStatus())
                .thumbnailUrl(course.getThumbnailUrl())
                .isActive(course.getIsActive())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
