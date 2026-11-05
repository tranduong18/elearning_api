package com.duong.elearning_api.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.duong.elearning_api.dto.response.FileResponse;
import com.duong.elearning_api.exception.AppException;
import com.duong.elearning_api.exception.ErrorCode;
import com.duong.elearning_api.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j(topic = "FILE-STORAGE-SERVICE")
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String DEFAULT_FOLDER = "elearning/courses";
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final Cloudinary cloudinary;

    @Override
    public FileResponse uploadImage(MultipartFile file) {
        // 1. Kiểm tra nếu file không có nội dung (0 byte)
        if(file.isEmpty()){
            throw new AppException(ErrorCode.FILE_EMPTY);
        }
        // 2. Kiểm tra dung lượng tối đa
        if(file.getSize() > MAX_FILE_SIZE){
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }
        // 3. Kiểm tra định dạng ảnh hợp lệ
        String contentType = file.getContentType();
        if(contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())){
            throw new AppException(ErrorCode.INVALID_IMAGE_FORMAT);
        }

        try {
            // 4. Tải tệp lên Cloudinary theo thư mục mặc định
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                        "folder", DEFAULT_FOLDER,
                        "resource_type", "image"
                    )
            );

            log.info("Upload image successful: publicId={}", uploadResult.get("public_id"));

            return FileResponse.builder()
                    .publicId((String) uploadResult.get("public_id"))
                    .secureUrl((String) uploadResult.get("secure_url"))
                    .format((String) uploadResult.get("format"))
                    .build();
        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary: ", e);
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }

    @Override
    public void deleteImage(String publicId) {
        if(publicId == null || publicId.isBlank()){
            return;
        }

        try {
            Map result = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap("resource_type", "image")
            );

            log.info("Deleted image from Cloudinary: publicId={}, result={}", publicId, result.get("result"));
        } catch (IOException e) {
            log.error("Failed to delete image from Cloudinary with publicId={}: ", publicId, e);
        }
    }
}
