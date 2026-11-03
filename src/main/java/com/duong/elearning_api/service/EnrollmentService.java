package com.duong.elearning_api.service;

import com.duong.elearning_api.dto.request.EnrollCourseRequest;
import com.duong.elearning_api.dto.response.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse enrollCourse(String userId, EnrollCourseRequest request);

    List<EnrollmentResponse> getMyEnrollments(String userId);
}
