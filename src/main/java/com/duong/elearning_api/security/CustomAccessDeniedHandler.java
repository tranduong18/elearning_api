package com.duong.elearning_api.security;

import com.duong.elearning_api.dto.response.ErrorResponse;
import com.duong.elearning_api.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.json.JsonMapper;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j(topic = "ACCESS-DENIED-HANDLER")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final JsonMapper jsonMapper = new JsonMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.error("Access denied: {}", accessDeniedException.getMessage());

        // 1. Đặt HTTP Status là 403 Forbidden
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 2. Định dạng phản hồi trả về là JSON UTF-8
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 3. Đóng gói thông báo lỗi theo chuẩn ErrorResponse của hệ thống
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .error(errorCode.getStatus().getReasonPhrase())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        // 4. Ghi trực tiếp chuỗi JSON ra luồng phản hồi
        response.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        response.flushBuffer();
    }
}
