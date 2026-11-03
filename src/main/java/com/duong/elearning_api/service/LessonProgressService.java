package com.duong.elearning_api.service;

import com.duong.elearning_api.dto.request.UpdateProgressRequest;
import com.duong.elearning_api.dto.response.CourseProgressResponse;

public interface LessonProgressService {
    void upsertProgress(String userId, UpdateProgressRequest request);

    CourseProgressResponse getCourseProgress(String userId, String courseId);
}
