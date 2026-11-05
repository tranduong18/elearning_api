package com.duong.elearning_api.service.impl;

import com.duong.elearning_api.common.CourseStatus;
import com.duong.elearning_api.config.CacheConfig;
import com.duong.elearning_api.dto.request.CreateCourseRequest;
import com.duong.elearning_api.dto.request.UpdateCourseRequest;
import com.duong.elearning_api.dto.response.*;
import com.duong.elearning_api.entity.Chapter;
import com.duong.elearning_api.entity.Course;
import com.duong.elearning_api.exception.AppException;
import com.duong.elearning_api.exception.ErrorCode;
import com.duong.elearning_api.repository.ChapterRepository;
import com.duong.elearning_api.repository.CourseRepository;
import com.duong.elearning_api.service.CourseService;
import com.duong.elearning_api.service.FileStorageService;
import com.duong.elearning_api.utils.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "COURSE-SERVICE")
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final FileStorageService fileStorageService;

    @Override
    @PreAuthorize("hasAuthority('USER')")
    @Transactional
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
                .thumbnailUrl(request.thumbnailUrl() != null ? request.thumbnailUrl().trim() : null)
                .isActive(true)
                .build();

        courseRepository.save(course);
        return mapToDetailResponse(course);
    }

    // Hàm cập nhật: Xóa key cache trong Redis ngay khi cập nhật Database thành công
    @Override
    @PreAuthorize("hasAuthority('USER')")
    @CacheEvict(value = CacheConfig.COURSE_CURRICULUM_CACHE, key = "#id")
    @Transactional
    public CourseDetailResponse updateCourse(String id, UpdateCourseRequest request) {
        log.info("==> [CACHE EVICT] Updating course and evicting cache for id: {}", id);

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

        if (request.thumbnailUrl() != null) {
            String newThumbnailUrl = request.thumbnailUrl().trim();
            String oldThumbnailUrl = course.getThumbnailUrl();

            // Nếu khóa học đã có ảnh cũ và ảnh mới khác ảnh cũ -> Tự động dọn dẹp ảnh cũ trên Cloudinary
            if(oldThumbnailUrl != null && !oldThumbnailUrl.isBlank() && !oldThumbnailUrl.equals(newThumbnailUrl)){
                String oldPublicId = CloudinaryUtils.extractPublicId(oldThumbnailUrl);
                if(oldPublicId != null){
                    log.info("Detected old thumbnail for courseId={}, proceeding to delete publicId={}", id, oldPublicId);
                    fileStorageService.deleteImage(oldPublicId);
                }
            }
            course.setThumbnailUrl(newThumbnailUrl);
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
    public List<CourseDetailResponse> getAllCoursesWithChapters() {
        List<Course> courses = courseRepository.findAllActiveCoursesWithChapters();
        return courses.stream()
                .map(this::mapToDetailResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @CacheEvict(value = CacheConfig.COURSE_CURRICULUM_CACHE, key = "#id")
    public void deleteCourse(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        course.setIsActive(false);
        courseRepository.save(course);
    }

    // Gắn @Cacheable: Toàn bộ cấu trúc khóa học được lưu trong cache COURSE_CURRICULUM_CACHE với key là id khóa học
    @Override
    @Cacheable(value = CacheConfig.COURSE_CURRICULUM_CACHE, key = "#courseId")
    @Transactional(readOnly = true)
    public CourseCurriculumResponse getCourseCurriculum(String courseId) {
        log.info("==> [DATABASE QUERY] Fetching course curriculum from database for courseId: {}", courseId);

        // Query 1: Lấy thông tin cơ bản của Course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // Query 2: Lấy toàn bộ Chapters và nạp sẵn toàn bộ Lessons (JOIN FETCH 1 câu SQL)
        List<Chapter> chapters = chapterRepository.findByCourseIdWithLessons(courseId);

        // Map sang DTO: Dữ liệu lessons đã nằm sẵn trong RAM, hoàn toàn không phát sinh thêm query nào!
        List<ChapterDetailResponse> chapterResponses = chapters.stream()
                .map(chapter -> {
                    List<LessonResponse> lessonResponses = chapter.getLessons() != null
                            ? chapter.getLessons().stream()
                            .map(lesson -> LessonResponse.builder()
                                    .id(lesson.getId())
                                    .title(lesson.getTitle())
                                    .videoUrl(lesson.getVideoUrl())
                                    .durationSeconds(lesson.getDurationSeconds())
                                    .isFreePreview(lesson.getIsFreePreview())
                                    .orderIndex(lesson.getOrderIndex())
                                    .build())
                            .toList()
                            : List.of();

                    return ChapterDetailResponse.builder()
                            .id(chapter.getId())
                            .title(chapter.getTitle())
                            .orderIndex(chapter.getOrderIndex())
                            .lessons(lessonResponses)
                            .build();
                })
                .toList();

        return CourseCurriculumResponse.builder()
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
