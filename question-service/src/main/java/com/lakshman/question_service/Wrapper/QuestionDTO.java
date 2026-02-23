package com.lakshman.question_service.Wrapper;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionDTO {
    @NotBlank(message = "Question cannot be empty")
    private String question;
    @NotBlank(message = "Option1 cannot be empty")
    private String option1;
    @NotBlank(message = "Option2 cannot be empty")
    private String option2;
    @NotBlank(message = "Option3 cannot be empty")
    private String option3;
    @NotBlank(message = "Option4 cannot be empty")
    private String option4;
    @NotBlank(message = "Answer cannot be empty")
    private String answer;
    @NotBlank(message = "Question Level cannot be empty")
    private String questionLevel;
    @NotBlank(message = "Category Name cannot be empty")
    private String categoryName;
}
