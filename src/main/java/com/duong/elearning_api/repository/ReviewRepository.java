package com.duong.elearning_api.repository;

import com.duong.elearning_api.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    // 1. Kiểm tra học viên đã từng đánh giá khóa học này hay chưa
    boolean existsByUserIdAndCourseId(String userId, String courseId);

    // 2. Đếm tổng số lượt đánh giá của một khóa học
    long countByCourseId(String courseId);

    // 3. Lấy danh sách các đánh giá mới nhất của khóa học
    List<Review> findByCourseIdOrderByCreatedAtDesc(String courseId);

    // 4. Tính điểm đánh giá trung bình của một khóa học (trả về 0.0 nếu chưa có đánh giá nào)
    @Query("select coalesce(avg(r.rating), 0.0) from Review r where r.course.id = :courseId")
    Double findAverageRatingByCourseId(@Param("courseId") String courseId);

    // 5. Lấy đánh giá của một học viên cụ thể trên một khóa học
    Optional<Review> findByUserIdAndCourseId(String userId, String courseId);
}
