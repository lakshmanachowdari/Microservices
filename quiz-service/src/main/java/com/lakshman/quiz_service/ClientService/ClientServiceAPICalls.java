package com.lakshman.quiz_service.ClientService;

import com.lakshman.quiz_service.Feing.QuestionInterface;
import com.lakshman.quiz_service.Utility.ApiResponse;
import com.lakshman.quiz_service.Utility.ResponseUtil;
import com.lakshman.quiz_service.Wrapper.QuestionIds;
import com.lakshman.quiz_service.Wrapper.QuestionWrapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import com.lakshman.quiz_service.Constants.MarketDataConstants;

@Slf4j
@Component
public class ClientServiceAPICalls {

    private final RestTemplate restTemplate;

    public ClientServiceAPICalls(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private QuestionInterface questionInterface;

    @CircuitBreaker(name = "questionService", fallbackMethod = "getQuestionsByQuizIdFallback")
    public ApiResponse<List<QuestionWrapper>> getQuestionsByQuizId(QuestionIds questionIds) {
        // Implementation to call the API and return the response
        HttpEntity<QuestionIds> requestEntity = new HttpEntity<>(questionIds);
        ResponseEntity<ApiResponse<List<QuestionWrapper>>> response = restTemplate.exchange(
                MarketDataConstants.GET_QUESTION_ID_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        log.info("API call to get questions by quiz ID completed with status code: {}", response.getStatusCode());
        return response.getBody();
    }
    public ApiResponse<List<QuestionWrapper>> getQuestionsByQuizIdFallback(QuestionIds questionIds, Throwable throwable) {
        log.error("API call to get questions by quiz ID failed: {}", throwable.getMessage());
        // Return a fallback response or an empty list
        ResponseEntity<ApiResponse<List<QuestionWrapper>>> response  = questionInterface.getQuestionsByIds(questionIds);
        log.info("Fallback API call to get questions by quiz ID completed: {}", response);
        return response.getBody();
    }

    /**
    public ResponseEntity<?> getQuestionsByQuizIdFallback(QuestionIds questionIds, Throwable throwable) {
        log.error("API call to get questions by quiz ID failed: {}", throwable.getMessage());
        // Return a fallback response or an empty list
        return ResponseUtil.serviceUnavailable( "Question Service is currently unavailable. Please try again later.");
    }
     */
}
