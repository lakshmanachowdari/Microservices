package com.lakshman.question_service.Controller;

import com.lakshman.question_service.Constants.MarketDataConstants;
import com.lakshman.question_service.Entity.TestResponse;
import com.lakshman.question_service.Service.QuestionService;
import com.lakshman.question_service.Wrapper.QuestionDTO;
import com.lakshman.question_service.Wrapper.QuestionIds;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("question")
public class QuestionController
{
    @Autowired
    private QuestionService questionService;

    @Autowired
    private Environment environment;

    // Fetch All Questions
    @GetMapping(path = MarketDataConstants.ALL_QUESTIONS, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllQuestions(){
        log.info("Port number: {}", environment.getProperty("local.server.port"));
        return questionService.getAllQuestions();
    }

    // Fetch Questions by question category
    @GetMapping(path = MarketDataConstants.QUESTION_BY_CATEGORY, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getQuestionById(@PathVariable(value = "category") String category) {
        try{
            return questionService.getQuestionByCategory(category);
        }
        catch (Exception e){
            log.error("Error while fetching questions for category: {}", category, e);
            return new ResponseEntity<>(questionService.getQuestionByCategory(category), HttpStatus.NO_CONTENT);
        }
    }

    // Add a single question
    @PostMapping(path = MarketDataConstants.ADD_QUESTION, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addQuestion(@Valid @RequestBody QuestionDTO question){
        return questionService.addQuestion(question);
    }

    //Add a list of questions
    @PostMapping(path = MarketDataConstants.ADD_QUESTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addListOfQuestions(@Valid @RequestBody List<QuestionDTO> questions){
        return questionService.addListOfQuestions(questions);
    }

    // Generate questions
    @GetMapping(path = MarketDataConstants.GENERATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateQuestionIds(@RequestParam(value = "category") String category,
                                                 @RequestParam(value = "numQ") Integer numQ) {
        return questionService.generateQuestionIds(category, numQ);
    }

    // Get Question (Based on quiz Id)
    @PostMapping(path = MarketDataConstants.GET_QUESTION_IDS, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getQuestionsByIds(@RequestBody QuestionIds questionIds){
        return questionService.getQuestionsByIds(questionIds.getQuestionIds());
    }

    // Score
    @PostMapping(path = MarketDataConstants.SCORE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getScore(@RequestBody List<TestResponse> submit){
        return questionService.getScore(submit);
    }

    // Score v1
    @PostMapping(path = MarketDataConstants.SCORE_V1, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getScoreV1(@RequestBody List<TestResponse> submit){
        return questionService.getScoreV1(submit);
    }

    @GetMapping(path = MarketDataConstants.CHECK_RESULT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkResult(@RequestParam(value = "ids") List<Integer> questionIds){
        return questionService.checkResult(questionIds);
    }

    @GetMapping(path = MarketDataConstants.DUPLICATE_QUESTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> duplicateQuestions() {
        return questionService.duplicateQuestions();
    }

    @PutMapping(path = MarketDataConstants.DELETE_DUPLICATES, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDuplicates() {
        return questionService.deleteDuplicates();
    }

    // Fetch response (Based on quiz Id)
    @PostMapping(path = MarketDataConstants.TEST_RESPONSE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getResponseByIds(@RequestBody QuestionIds questionIds){
        return questionService.getResponsesByIds(questionIds.getQuestionIds());
    }
}
