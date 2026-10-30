package com.duong.elearning_api.service.impl;

import com.duong.elearning_api.common.CourseStatus;
import com.duong.elearning_api.dto.request.CreateCourseRequest;
import com.duong.elearning_api.dto.request.UpdateCourseRequest;
import com.duong.elearning_api.dto.response.ChapterResponse;
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
    public CourseDetailResponse updateCourse(String id, UpdateCourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        if (request.title() != null) {
            course.setTitle(request.title());
        }
        if (request.description() != null) {
            course.setDescription(request.description());
        }
        if (request.price() != null) {
            course.setPrice(request.price());
        }
        if (request.level() != null) {
            course.setLevel(request.level());
        }
        if (request.status() != null) {
            course.setStatus(request.status());
        }

        courseRepository.save(course);
        return mapToDetailResponse(course);
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
        List<ChapterResponse> chapterResponses = course.getChapters() != null
                ? course.getChapters().stream()
                  .map(chapter -> ChapterResponse.builder()
                          .id(chapter.getId())
                          .title(chapter.getTitle())
                          .orderIndex(chapter.getOrderIndex())
                          .build()).toList()
                : List.of();

        return CourseDetailResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .level(course.getLevel())
                .status(course.getStatus())
                .thumbnailUrl(course.getThumbnailUrl())
                .isActive(course.getIsActive())
                .chapters(chapterResponses)
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
