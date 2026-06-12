package com.lakshman.quiz_service.Wrapper;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class QuestionIds {

    @NotNull(message = "Question IDs cannot be null")
    private List<Integer> questionIds;

    public QuestionIds(List<Integer> questionIds) {
        this.questionIds = questionIds;
    }
}
