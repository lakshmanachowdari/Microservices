package com.lakshman.quiz_service.Utility;

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

    private static <T> ResponseEntity<ApiResponse<T>> build(
            HttpStatus status,
            String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .code(status.name())
                .message(message)
                .data(null)
                .errors(null)
                .build();

        return new ResponseEntity<>(response, status);
    }

    // 200 OK
    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return build(HttpStatus.OK, message, data);
    }

    // 200 OK
    public static <T> ResponseEntity<ApiResponse<T>> ok(String message) {
        return build(HttpStatus.OK, message);
    }

    // 201 CREATED
    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return build(HttpStatus.CREATED, message, data);
    }

    // 202 ACCEPTED
    public static <T> ResponseEntity<ApiResponse<T>> accepted(String message, T data) {
        return build(HttpStatus.ACCEPTED, message, data);
    }

    // 204 SUCCESSFUL NO CONTENT
    public static <T> ResponseEntity<ApiResponse<T>> noContent(String message, T data) {
        return build(HttpStatus.NO_CONTENT, message, data);
    }

    // 400 BAD REQUEST
    public static <T> ResponseEntity<ApiResponse<T>> serviceUnavailable(String message) {
        return build(HttpStatus.BAD_REQUEST, message);
    }

    // 404 NOT FOUND
    public static <T> ApiResponse<T> serviceNotAvailable(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .code(HttpStatus.NOT_FOUND.name())
                .message(message)
                .data(null)
                .errors(null)
                .build();
    }
}
