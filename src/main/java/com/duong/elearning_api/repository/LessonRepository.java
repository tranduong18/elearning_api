package com.duong.elearning_api.repository;

import com.duong.elearning_api.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, String> {
    List<Lesson> findAllByChapterIdOrderByOrderIndexAsc(String chapterId);

    long countByChapterId(String chapterId);
}
