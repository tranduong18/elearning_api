package com.duong.elearning_api.service;

import com.duong.elearning_api.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileResponse uploadImage(MultipartFile file);

    void deleteImage(String publicId);
}
