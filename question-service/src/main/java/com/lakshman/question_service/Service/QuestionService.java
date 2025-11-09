package com.lakshman.question_service.Service;

import com.lakshman.question_service.Entity.Question;
import com.lakshman.question_service.Entity.SubmitResult;
import com.lakshman.question_service.Repository.QuestionJdbcRepository;
import com.lakshman.question_service.Repository.QuestionJpaRepository;
import com.lakshman.question_service.Wrapper.QuestionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionService {

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Autowired
    QuestionJdbcRepository questionJdbcRepository;

    public List<Question> getAllQuestions() {
        return questionJpaRepository.findAll();
    }

    public ResponseEntity<List<Question>> getQuestionByCategory(String category) {
        List<Question> questions = new ArrayList<>();
        try{
            questions = questionJpaRepository.findByCategory(category);
            if(!CollectionUtils.isEmpty(questions)){
                return new ResponseEntity<>(questions, HttpStatus.OK);
            }
        }
        catch (Exception e){
            log.error("Error while fetching questions for category: {}", category, e);
        }
        return new ResponseEntity<>(questions, HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        questionJpaRepository.save(question);
        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
    }

    public ResponseEntity<String> addListOfQuestions(List<Question> questions) {
        for(Question q : questions){
            questionJpaRepository.save(q);
        }
        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
    }

    public ResponseEntity<List<Integer>> generateQuestionIds(String category, Integer numQ) {
        List<Integer> questionIds = questionJpaRepository.getRandomQuestionIdsByCategory(category, numQ);
        return new ResponseEntity<>(questionIds, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsByIds(List<Integer> questionIds) {
        List<Question> questionList  = new ArrayList<>();
        for(Integer id:questionIds){
            questionList.add(questionJpaRepository.findById(id).get());
        }

        List<QuestionWrapper> questionWrappers = questionList.stream().map(
                q-> new QuestionWrapper(q.getId(),
                q.getQuestion(), q.getOption1(),q.getOption2(), q.getOption3(),q.getOption4()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<SubmitResult> submit) {
//        List<Integer> ids = submit.stream().map(SubmitResult::getId).toList();
//        List<SubmitResult> correctAns = questionRepository.getCorrectAnsByIds(ids);
        int count=0;
        for(SubmitResult q : submit){
            Question questionDB = questionJpaRepository.findById(q.getId()).get();
            if(q.getResponse().equals(questionDB.getAnswer())){
                count++;
            }
        }
        System.out.print("Quiz Score: ");
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    public ResponseEntity<List<SubmitResult>> checkResult(List<Integer> questionIds) {
        List<SubmitResult> response =  questionJdbcRepository.getCorrectAnsByIds(questionIds);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
