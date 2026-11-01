package com.duong.elearning_api.repository;

import com.duong.elearning_api.entity.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    boolean existsByTitleIgnoreCase(String title);

    @Query("select distinct c from Course c left join fetch c.chapters where c.isActive = true")
    List<Course> findAllActiveCoursesWithChapters();

    @EntityGraph(attributePaths = "chapters")
    @Query("select c from Course c where c.isActive = true")
    List<Course> findAllWithChaptersGraph();
}
