package com.lakshman.quiz_service.Feing;

import com.lakshman.quiz_service.Constants.MarketDataConstants;
import com.lakshman.quiz_service.Utility.ApiResponse;
import com.lakshman.quiz_service.Wrapper.QuestionIds;
import com.lakshman.quiz_service.Wrapper.QuestionWrapper;
import com.lakshman.quiz_service.Wrapper.TestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "QUESTION-SERVICE")
public interface QuestionInterface {

    // Get question ids
    @GetMapping(path = MarketDataConstants.GENERATE_QUESTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiResponse<List<Integer>>> generateQuestionIds(@RequestParam String category,
                                                                   @RequestParam Integer numQ);
    // Get Question (Based on quiz Id)
    @PostMapping(path = MarketDataConstants.GET_QUESTION_IDS, produces =  MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiResponse<List<QuestionWrapper>>> getQuestionsByIds(@RequestBody QuestionIds questionIds);

    // Score
    @PostMapping(path = MarketDataConstants.GET_RESPONSE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiResponse<Integer>> getScore(@RequestBody List<TestResponse> submit);

    // Score V1
    @PostMapping(path = MarketDataConstants.GET_RESPONSE_V1, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiResponse<Integer>> getV1Score(@RequestBody List<TestResponse> submit);

    @GetMapping(path = MarketDataConstants.GET_TEST_RESPONSE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiResponse<List<TestResponse>>> getResponse(@RequestParam(value = "ids") List<Integer> questionIds);

}
