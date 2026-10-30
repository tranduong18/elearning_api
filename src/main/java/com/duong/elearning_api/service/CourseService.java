package com.duong.elearning_api.service;

import com.duong.elearning_api.dto.request.CreateCourseRequest;
import com.duong.elearning_api.dto.request.UpdateCourseRequest;
import com.duong.elearning_api.dto.response.CourseDetailResponse;
import com.duong.elearning_api.entity.Course;

import java.util.List;

public interface CourseService {
    CourseDetailResponse createCourse(CreateCourseRequest request);

    CourseDetailResponse updateCourse(String id, UpdateCourseRequest course);

    CourseDetailResponse getCourseById(String id);

    List<Course> getAllCourses();

    void deleteCourse(String id);
}
