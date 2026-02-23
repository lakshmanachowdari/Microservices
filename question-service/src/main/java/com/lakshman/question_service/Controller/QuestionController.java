package com.lakshman.question_service.Controller;

import com.lakshman.question_service.Constants.MarketDataConstants;
import com.lakshman.question_service.Entity.Question;
import com.lakshman.question_service.Entity.SubmitResult;
import com.lakshman.question_service.Service.QuestionService;
import com.lakshman.question_service.Wrapper.QuestionDTO;
import com.lakshman.question_service.Wrapper.QuestionIds;
import com.lakshman.question_service.Wrapper.QuestionWrapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("question")
public class QuestionController
{
    @Autowired
    QuestionService questionService;

    @Autowired
    Environment environment;

    @GetMapping(path = MarketDataConstants.ALL_QUESTIONS, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Question>> getAllQuestions(){
        log.info("Port number: {}", environment.getProperty("local.server.port"));
        return new ResponseEntity<>(questionService.getAllQuestions(), HttpStatus.OK);
    }

    @GetMapping(path = MarketDataConstants.QUESTION_BY_CATEGORY, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Question>> getQuestionById( @PathVariable(value = "category") String category) throws Exception {
        try{
            return new ResponseEntity<>(questionService.getQuestionByCategory(category), HttpStatus.OK);
        }
        catch (Exception e){
            log.error("Error while fetching questions for category: {}", category, e);
            return new ResponseEntity<>(questionService.getQuestionByCategory(category), HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(path = MarketDataConstants.ADD_QUESTION, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addQuestion(@Valid @RequestBody QuestionDTO question){
        String response  = questionService.addQuestion(question);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(path = MarketDataConstants.ADD_QUESTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addListOfQuestions(@Valid @RequestBody List<QuestionDTO> questions){
        String response = questionService.addListOfQuestions(questions);;
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Generate questions
    @GetMapping(path = MarketDataConstants.GENERATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> generateQuestionIds(@RequestParam(value = "category") String category,
                                                             @RequestParam(value = "numQ") Integer numQ) throws Exception {
        return new ResponseEntity<>(questionService.generateQuestionIds(category, numQ), HttpStatus.OK);
    }

    // Get Question (Based on quiz Id)
    @GetMapping(path = MarketDataConstants.GET_QUESTION_IDS, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionWrapper>> getQuestionsByIds(@RequestBody QuestionIds questionIds){
        return new ResponseEntity<>(questionService.getQuestionsByIds(questionIds.getQuestionIds()), HttpStatus.OK);
    }

    // Score
    @PostMapping(path = MarketDataConstants.SCORE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getScore(@RequestBody List<SubmitResult> submit){
        return new ResponseEntity<>(questionService.getScore(submit), HttpStatus.OK);
    }

    // Score v1
    @PostMapping(path = MarketDataConstants.SCORE_V1, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getScoreV1(@RequestBody List<SubmitResult> submit){
        return new ResponseEntity<>(questionService.getScoreV1(submit), HttpStatus.OK);
    }

    @GetMapping(path = MarketDataConstants.CHECK_RESULT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SubmitResult>> checkResult(@RequestParam(value = "ids") List<Integer> questionIds){
        return new ResponseEntity<>(questionService.checkResult(questionIds), HttpStatus.OK);
    }

    @GetMapping(path = MarketDataConstants.DUPLICATE_QUESTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionWrapper>> duplicateQuestions() {
        return new ResponseEntity<>(questionService.duplicateQuestions(), HttpStatus.OK);
    }

    @PutMapping(path = MarketDataConstants.DELETE_DUPLICATES, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteDuplicates() {
        String response = questionService.deleteDuplicates();
        System.out.println(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
