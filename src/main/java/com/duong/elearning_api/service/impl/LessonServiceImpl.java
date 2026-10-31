package com.duong.elearning_api.service.impl;

import com.duong.elearning_api.dto.request.CreateLessonRequest;
import com.duong.elearning_api.dto.response.LessonResponse;
import com.duong.elearning_api.entity.Chapter;
import com.duong.elearning_api.entity.Lesson;
import com.duong.elearning_api.exception.AppException;
import com.duong.elearning_api.exception.ErrorCode;
import com.duong.elearning_api.repository.ChapterRepository;
import com.duong.elearning_api.repository.LessonRepository;
import com.duong.elearning_api.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "LESSON-SERVICE")
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;

    @Override
    public List<LessonResponse> getLessonByChapterId(String chapterId) {
        return lessonRepository.findAllByChapterIdOrderByOrderIndexAsc(chapterId)
                .stream().map(this::mapToResponse)
                .toList();
    }

    @Override
    public LessonResponse createLesson(CreateLessonRequest request) {
        Chapter chapter = chapterRepository.findById(request.chapterId())
                .orElseThrow(() -> new AppException(ErrorCode.CHAPTER_NOT_FOUND));

        int nextOrderIndex = (int) lessonRepository.countByChapterId(chapter.getId()) + 1;

        Lesson lesson = Lesson.builder()
                .title(request.title())
                .videoUrl(request.videoUrl())
                .durationSeconds(request.durationSeconds() != null ? request.durationSeconds() : 0)
                .isFreePreview(request.isFreePreview() != null ? request.isFreePreview() : false)
                .orderIndex(nextOrderIndex)
                .chapter(chapter)
                .build();

        lessonRepository.save(lesson);
        return mapToResponse(lesson);
    }

    private LessonResponse mapToResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .videoUrl(lesson.getVideoUrl())
                .durationSeconds(lesson.getDurationSeconds())
                .isFreePreview(lesson.getIsFreePreview())
                .orderIndex(lesson.getOrderIndex())
                .build();
    }
}
