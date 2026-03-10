package com.lakshman.quiz_service.Service;

import com.lakshman.quiz_service.Entity.Quiz;
import com.lakshman.quiz_service.Exception.ResourceNotFoundException;
import com.lakshman.quiz_service.Feing.QuestionInterface;
import com.lakshman.quiz_service.Repository.QuizJpaRepository;
import com.lakshman.quiz_service.Utility.ApiResponse;
import com.lakshman.quiz_service.Utility.ResponseUtil;
import com.lakshman.quiz_service.Wrapper.QuestionIds;
import com.lakshman.quiz_service.Wrapper.QuestionWrapper;
import com.lakshman.quiz_service.Wrapper.TestResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class QuizService {

    @Autowired
    private QuizJpaRepository quizJapRepository;

    @Autowired
    private QuestionInterface questionInterface;

    @CircuitBreaker(name = "createQizBreaker", fallbackMethod = "createQuizFallback")
    public ResponseEntity<?> createQuiz(String quizTitle, int numQ, String category) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizTitle);
        ApiResponse<List<Integer>> response = questionInterface.generateQuestionIds(category, numQ).getBody();
        if (response == null || !response.isSuccess()) {
            throw new RuntimeException("Invalid response from Question Service");
        }
        quiz.setQuestionIds(response.getData());
        return ResponseUtil.created("Quiz for " + category + " created", quizJapRepository.save(quiz));
    }

    private ResponseEntity<ApiResponse<String>> createQuizFallback(String quizTitle, int numQ, String category, Throwable throwable) {
        return ResponseUtil.serviceUnavailable("Question service unavailable. Please try later.");
    }

    public ResponseEntity<?> getQuiz(Integer id) {
        List<Integer> questionIds = quizJapRepository.findById(id).orElseThrow().getQuestionIds();
        QuestionIds ids = new QuestionIds(questionIds);
        ApiResponse<List<QuestionWrapper>> questionWrappers = questionInterface.getQuestionsByIds(ids).getBody();
        return ResponseUtil.ok("Questions for the Quiz ID " + id, questionWrappers);
    }

    @Retry(name = "retryQuizScore", fallbackMethod = "retryQuizScoreFallback")
    public ResponseEntity<?> submitResult(Integer id, List<TestResponse> testResponses) {
        ApiResponse<Integer> response = questionInterface.getScore(testResponses).getBody();

        if (response == null || !response.isSuccess())
            throw new RuntimeException("Invalid response from Question Service");

        return ResponseUtil.ok(response.getData() + " Out of " + testResponses.size() + " for test Id: " + id, response.getData());
    }

    public ResponseEntity<?> retryQuizScoreFallback(Integer id, List<TestResponse> testResponses, Throwable ex) {
        ApiResponse<Integer> response = questionInterface.getV1Score(testResponses).getBody();

        if (response == null || !response.isSuccess())
            return ResponseUtil.serviceUnavailable("Question service unavailable. Please try later.");

        return ResponseUtil.ok(response.getData() + " Out of " + testResponses.size() + " for test Id: " + id, response.getData());
    }

    public ResponseEntity<?> getAllQuiz() {
        try {
            List<Quiz> allQuiz = quizJapRepository.findAll();
            return ResponseUtil.ok("Total quiz's available are " + allQuiz.size(), allQuiz);
        } catch (Exception e) {
            throw new RuntimeException("Please try again some time later");
        }
    }

    public ResponseEntity<?> testResponseById(Integer id) {
        List<Integer> questionIds = quizJapRepository.findById(id).orElseThrow().getQuestionIds();
        List<TestResponse> responses = Objects.requireNonNull(questionInterface.getResponse(questionIds).getBody()).getData();
        return ResponseUtil.ok("Test Response for Quiz ID: " + id, responses);
    }
}
