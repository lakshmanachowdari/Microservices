package com.lakshman.quiz_service.Wrapper;

import lombok.Data;

@Data
public class CreateRequest {

    private String category;
    private String quizTitle;
    private Integer numQ;
}
