package com.duong.elearning_api.repository;

import com.duong.elearning_api.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    // Lấy danh sách chương của một khóa học, sắp xếp theo thứ tự hiển thị tăng dần
    List<Chapter> findAllByCourseIdOrderByOrderIndexAsc(String courseId);

    // Đếm số chương hiện có của khóa học để tính thứ tự hiển thị tiếp theo
    long countByCourseId(String courseId);
}
