package com.duong.elearning_api.service.impl;

import com.duong.elearning_api.dto.request.CreateChapterRequest;
import com.duong.elearning_api.dto.response.ChapterResponse;
import com.duong.elearning_api.entity.Chapter;
import com.duong.elearning_api.entity.Course;
import com.duong.elearning_api.exception.AppException;
import com.duong.elearning_api.exception.ErrorCode;
import com.duong.elearning_api.repository.ChapterRepository;
import com.duong.elearning_api.repository.CourseRepository;
import com.duong.elearning_api.service.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CHAPTER-SERVICE")
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;

    @Override
    public ChapterResponse createChapter(CreateChapterRequest request) {
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // Tự động tính orderIndex tiếp theo: đếm số chương hiện có + 1
        int nextOrderIndex = (int) chapterRepository.countByCourseId(course.getId()) + 1;

        Chapter chapter = Chapter.builder()
                .title(request.title())
                .orderIndex(nextOrderIndex)
                .course(course)
                .build();

        chapterRepository.save(chapter);
        return mapToChapterResponse(chapter);
    }

    @Override
    public List<ChapterResponse> getChaptersByCourseId(String courseId) {
        return chapterRepository.findAllByCourseIdOrderByOrderIndexAsc(courseId)
                .stream().map(this::mapToChapterResponse)
                .toList();
    }

    private ChapterResponse mapToChapterResponse(Chapter chapter) {
        return ChapterResponse.builder()
                .id(chapter.getId())
                .title(chapter.getTitle())
                .orderIndex(chapter.getOrderIndex())
                .build();
    }
}
