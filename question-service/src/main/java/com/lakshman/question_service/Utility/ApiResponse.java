package com.lakshman.question_service.Utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ApiResponse <T> {
    private boolean success;
    private LocalDateTime timestamp;
    private int status;
    private String code;
    private String message;
    private T data;
    private Object errors;
}
