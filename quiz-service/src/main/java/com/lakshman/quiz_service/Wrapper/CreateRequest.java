package com.lakshman.quiz_service.Wrapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRequest {

    @NotBlank(message = "Category can't be null")
    private String category;
    @NotBlank(message = "Quiz title can't be null")
    private String quizTitle;
    @NotNull(message = "Question count can't be null")
    private Integer numQ;
}
