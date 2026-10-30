package com.duong.elearning_api.exception;

import com.duong.elearning_api.dto.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j(topic = "GLOBAL-EXCEPTION")
public class GlobalExceptionHandler {
    // Bắt các Exception nghiệp vụ do chúng ta chủ động ném ra
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException exception, WebRequest request) {
        ErrorCode errorCode = exception.getErrorCode();
        String path = request.getDescription(false).replace("uri=", "");

        log.error("AppException occurred: code={}, message={}, path={}",
                errorCode.getCode(), errorCode.getMessage(), path);

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .error(errorCode.getStatus().getReasonPhrase())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    // 2. Bắt lỗi sai quy chuẩn dữ liệu đầu vào (@Valid)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, WebRequest request
    ) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errors = fieldErrors.stream().map(FieldError::getDefaultMessage).toList();

        String path = request.getDescription(false).replace("uri=", "");
        log.warn("Validation failed on path={}: {}", path, errors);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(errors.size() > 1 ? String.valueOf(errors) : errors.getFirst())
                .path(path)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 3. Bắt toàn bộ các lỗi máy chủ chưa lường trước (fallback 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUncategorizedException(
            Exception exception, WebRequest request
    ) {
        String path = request.getDescription(false).replace("uri=", "");
        log.error("Uncategorized exception occurred on path={}: ", path, exception);

        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .error(errorCode.getStatus().getReasonPhrase())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }
}
