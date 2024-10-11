package com.lakshman.question_service.Service;

import com.lakshman.question_service.Entity.Question;
import com.lakshman.question_service.Entity.SubmitResult;
import com.lakshman.question_service.Repository.QuestionRepository;
import com.lakshman.question_service.Wrapper.QuestionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public ResponseEntity<List<Question>> getQuestionByCategory(String category) {
        List<Question> questions = new ArrayList<>();
        try{
            questions = questionRepository.findByCategory(category);
            if(!CollectionUtils.isEmpty(questions)){
                return new ResponseEntity<>(questions, HttpStatus.OK);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(questions, HttpStatus.NO_CONTENT);
    }

    public String addQuestion(Question question) {
        questionRepository.save(question);
        return "success";
    }

    public String addListOfQuestions(List<Question> questions) {
        for(Question q : questions){
            questionRepository.save(q);
        }
        return "success";
    }

    public ResponseEntity<List<Integer>> generateQuestionIds(String category, Integer numQ) {
        List<Integer> questionIds = questionRepository.getRandomQuestionIdsByCategory(category, numQ);
        return new ResponseEntity<>(questionIds, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsByIds(List<Integer> questionIds) {
        List<Question> questionList  = new ArrayList<>();
        for(Integer id:questionIds){
            questionList.add(questionRepository.findById(id).get());
        }

        List<QuestionWrapper> questionWrappers = questionList.stream().map(q-> new QuestionWrapper(q.getId(),
                q.getQuestion(), q.getOption1(),q.getOption2(), q.getOption3(),q.getOption4()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<SubmitResult> submit) {
//        List<Integer> ids = submit.stream().map(SubmitResult::getId).toList();
//        List<SubmitResult> correctAns = questionRepository.getCorrectAnsByIds(ids);
        int count=0;
        for(SubmitResult q : submit){
            Question questionDB = questionRepository.findById(q.getId()).get();
            if(q.getResponse().equals(questionDB.getAnswer())){
                count++;
            }
        }
        System.out.print("Quiz Score: ");
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
