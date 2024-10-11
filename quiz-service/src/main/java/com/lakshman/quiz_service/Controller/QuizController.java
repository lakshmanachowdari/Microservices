package com.lakshman.quiz_service.Controller;
import com.lakshman.quiz_service.Constants.MarketDataConstants;
import com.lakshman.quiz_service.Entity.Quiz;
import com.lakshman.quiz_service.Wrapper.SubmitResult;
import com.lakshman.quiz_service.Service.QuizService;
import com.lakshman.quiz_service.Wrapper.CreateRequest;
import com.lakshman.quiz_service.Wrapper.QuestionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
@Slf4j
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping(value = MarketDataConstants.CREATE_QUIZ,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createQuiz(@RequestBody CreateRequest request){
        return quizService.createQuiz(request.getQuizTitle(), request.getNumQ(), request.getCategory());
    }

    @GetMapping(value = MarketDataConstants.GET_QUIZ, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionWrapper>> getQuiz(@PathVariable Integer id){
        return quizService.getQuiz(id);
    }

    @PostMapping(value = MarketDataConstants.SUBMIT_RESULT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> submitResult(@PathVariable Integer id,
                                                @RequestBody List<SubmitResult> submitResult){
        return quizService.submitResult(id, submitResult);
    }

    @GetMapping(value = MarketDataConstants.GET_ALL_QUIZ, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Quiz>> getAllQuiz(){
        return quizService.getAllQuiz();
    }
}
