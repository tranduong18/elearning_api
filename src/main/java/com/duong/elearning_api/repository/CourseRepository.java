package com.duong.elearning_api.repository;

import com.duong.elearning_api.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    boolean existsByTitleIgnoreCase(String title);
}
