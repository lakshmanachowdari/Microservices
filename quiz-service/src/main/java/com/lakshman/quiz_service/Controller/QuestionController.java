package com.lakshman.quiz_service.Controller;

import com.lakshman.quiz_service.ClientService.ClientServiceAPICalls;
import com.lakshman.quiz_service.Utility.ApiResponse;
import com.lakshman.quiz_service.Wrapper.QuestionIds;
import com.lakshman.quiz_service.Wrapper.QuestionWrapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class QuestionController {

    @Autowired
    private ClientServiceAPICalls clientServiceAPICalls;

    @PostMapping(value = "/getQuestions", produces = "application/json")
    public ApiResponse<List<QuestionWrapper>> getQuestions(@Valid @RequestBody QuestionIds questionIds) {
        return clientServiceAPICalls.getQuestionsByQuizId(questionIds);
    }
}
