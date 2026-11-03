package com.duong.elearning_api.repository;

import com.duong.elearning_api.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
    // Kiểm tra học viên đã đăng ký khóa học này hay chưa
    boolean existsByUserIdAndCourseId(String userId, String courseId);

    // Lấy danh sách khóa học đã đăng ký của học viên, sắp xếp mới nhất lên đầu
    List<Enrollment> findAllByUserIdOrderByCreatedAtDesc(String userId);
}
