package com.lakshman.quiz_service.Controller;

import com.lakshman.quiz_service.Constants.MarketDataConstants;
import com.lakshman.quiz_service.Service.QuizService;
import com.lakshman.quiz_service.Wrapper.CreateRequest;
import com.lakshman.quiz_service.Wrapper.TestResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("quiz")
@Slf4j
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private Environment environment;

    @PostMapping(value = MarketDataConstants.CREATE_QUIZ,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createQuiz(@Valid @RequestBody CreateRequest request){
        log.info("Port number: {}", environment.getProperty("local.server.port"));
        return quizService.createQuiz(request.getQuizTitle(), request.getNumQ(), request.getCategory());
    }

    @GetMapping(value = MarketDataConstants.GET_QUIZ, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getQuiz(@PathVariable Integer id){
        return quizService.getQuiz(id);
    }

    @PostMapping(value = MarketDataConstants.SUBMIT_RESULT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submitResult(@PathVariable Integer id, @RequestBody List<TestResponse> testResponses){
        return quizService.submitResult(id, testResponses);
    }

    @GetMapping(value = MarketDataConstants.GET_ALL_QUIZ, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllQuiz(){
        return quizService.getAllQuiz();
    }

    @GetMapping(value = MarketDataConstants.TEST_RESPONSE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> testResponseById(@PathVariable Integer id){
        return quizService.testResponseById(id);
    }

}
