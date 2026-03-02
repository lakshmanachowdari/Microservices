package com.lakshman.quiz_service.Wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class QuestionIds {

    private List<Integer> questionIds;

    public QuestionIds(List<Integer> questionIds) {
        this.questionIds = questionIds;
    }
}
