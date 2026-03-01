package com.lakshman.question_service.Utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;

public class ResponseUtil {

        private static <T> ResponseEntity<ApiResponse<T>> build(
                HttpStatus status,
                String message,
                T data) {

            ApiResponse<T> response = ApiResponse.<T>builder()
                    .success(true)
                    .timestamp(LocalDateTime.now())
                    .status(status.value())
                    .code(status.name())
                    .message(message)
                    .data(data)
                    .errors(null)
                    .build();

            return new ResponseEntity<>(response, status);
        }

        // 200 OK
        public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
            return build(HttpStatus.OK, message, data);
        }

        // 201 CREATED
        public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
            return build(HttpStatus.CREATED, message, data);
        }

        // 202 ACCEPTED
        public static <T> ResponseEntity<ApiResponse<T>> accepted(String message, T data) {
            return build(HttpStatus.ACCEPTED, message, data);
        }

    // 204 ACCEPTED
     public static <T> ResponseEntity<ApiResponse<T>> noContent(String message, T data) {
        return build(HttpStatus.NO_CONTENT, message, data);
    }
}
