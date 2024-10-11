package com.lakshman.quiz_service.Feing;


import com.lakshman.quiz_service.Constants.MarketDataConstants;
import com.lakshman.quiz_service.Wrapper.SubmitResult;
import com.lakshman.quiz_service.Wrapper.QuestionIds;
import com.lakshman.quiz_service.Wrapper.QuestionWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("QUESTION-SERVICE")
public interface QuizInterface {

    //Get question ids
    @GetMapping(path = MarketDataConstants.GENERATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> generateQuestionIds(@RequestParam String category,
                                                             @RequestParam Integer numQ);

    // Get Question (Based on quiz Id)
    @PostMapping(path = MarketDataConstants.GET_QUESTION_IDS, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionWrapper>> getQuestionsByIds(@RequestBody QuestionIds questionIds);

    // Score
    @PostMapping(path = MarketDataConstants.GET_RESPONSE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getScore(@RequestBody List<SubmitResult> submit);

}
