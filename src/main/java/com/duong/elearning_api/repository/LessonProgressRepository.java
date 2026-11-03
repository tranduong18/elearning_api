package com.duong.elearning_api.repository;

import com.duong.elearning_api.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, String> {
    // 1. Tìm kiếm tiến độ cụ thể của học viên trên 1 bài học (dùng cho cơ chế Upsert)
    Optional<LessonProgress> findByUserIdAndLessonId(String userId, String lessonId);

    // 2. Đếm tổng số bài học đã hoàn thành của học viên trong một khóa học cụ thể
    @Query("""
        select count(lp) from LessonProgress lp
            where lp.user.id = :userId
            and lp.lesson.chapter.course.id = :courseId
            and lp.isCompleted = true
    """)
    long countCompletedLessons(@Param("userId") String userId, @Param("courseId") String courseId);
}
