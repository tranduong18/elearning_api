package com.duong.elearning_api.repository;

import com.duong.elearning_api.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    List<Chapter> findAllByCourseIdOrderByOrderIndexAsc(String courseId);

    long countByCourseId(String courseId);

    @Query("select distinct ch from Chapter ch left join fetch ch.lessons where ch.course.id = :courseId order by ch.orderIndex asc")
    List<Chapter> findByCourseIdWithLessons(@Param("courseId") String courseId);
}
