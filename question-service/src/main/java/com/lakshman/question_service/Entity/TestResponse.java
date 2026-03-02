package com.lakshman.question_service.Entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResponse {

    @NotBlank(message = "Question Id can't be null")
    private int id;
    private String response;
}
