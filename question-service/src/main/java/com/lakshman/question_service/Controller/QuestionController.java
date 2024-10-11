package com.lakshman.question_service.Controller;

import com.lakshman.question_service.Constants.MarketDataConstants;
import com.lakshman.question_service.Entity.Question;
import com.lakshman.question_service.Entity.SubmitResult;
import com.lakshman.question_service.Service.QuestionService;
import com.lakshman.question_service.Wrapper.QuestionIds;
import com.lakshman.question_service.Wrapper.QuestionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("question")
public class QuestionController
{
    @Autowired
    QuestionService questionService;
    @GetMapping(path = MarketDataConstants.ALL_QUESTIONS, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Question>> getAllQuestions(){
        return new ResponseEntity<>(questionService.getAllQuestions(), HttpStatus.OK);
    }

    @GetMapping(path = MarketDataConstants.QUESTION_BY_CATEGORY, produces =  MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<List<Question>> getQuestionById( @PathVariable(value = "category") String category) {
        return questionService.getQuestionByCategory(category);
    }

    @PostMapping(path = MarketDataConstants.ADD_QUESTION, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addQuestion(@RequestBody Question question){
        return new ResponseEntity<>(questionService.addQuestion(question), HttpStatus.CREATED);
    }

    @PostMapping(path = MarketDataConstants.ADD_QUESTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addListOfQuestions(@RequestBody List<Question> questions){
        return  new ResponseEntity<>(questionService.addListOfQuestions(questions), HttpStatus.CREATED);
    }

    // Generate questions
    @GetMapping(path = MarketDataConstants.GENERATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> generateQuestionIds(@RequestParam String category,
                                                             @RequestParam Integer numQ){
        return questionService.generateQuestionIds(category, numQ);
    }

    // Get Question (Based on quiz Id)
    @PostMapping(path = MarketDataConstants.GET_QUESTION_IDS, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionWrapper>> getQuestionsByIds(@RequestBody QuestionIds questionIds){
        return questionService.getQuestionsByIds(questionIds.getQuestionIds());
    }
    // Score
    @PostMapping(path = MarketDataConstants.GET_RESPONSE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getScore(@RequestBody List<SubmitResult> submit){
        return questionService.getScore(submit);
    }
}
