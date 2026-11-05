package com.duong.elearning_api.controller;

import com.duong.elearning_api.dto.response.ApiResponse;
import com.duong.elearning_api.dto.response.FileResponse;
import com.duong.elearning_api.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<FileResponse> uploadFile(
            @RequestParam("file") MultipartFile file
    ){
        FileResponse data = fileStorageService.uploadImage(file);
        return ApiResponse.<FileResponse>builder()
                .code(200)
                .message("Upload file successfully")
                .data(data)
                .build();
    }
}
