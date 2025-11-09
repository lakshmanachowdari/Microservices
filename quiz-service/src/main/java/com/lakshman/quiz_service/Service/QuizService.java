package com.lakshman.quiz_service.Service;

import com.lakshman.quiz_service.Entity.Quiz;
import com.lakshman.quiz_service.Wrapper.SubmitResult;
import com.lakshman.quiz_service.Feing.QuizInterface;
import com.lakshman.quiz_service.Repository.QuizRepository;
import com.lakshman.quiz_service.Wrapper.QuestionIds;
import com.lakshman.quiz_service.Wrapper.QuestionWrapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuizInterface quizInterface;

    @CircuitBreaker(name  = "createQizBreaker", fallbackMethod = "createQuizFallback")
    public ResponseEntity<String> createQuiz(String quizTitle, int numQ, String category) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizTitle);
        List<Integer> questionIds = quizInterface.generateQuestionIds(category, numQ).getBody();
        quiz.setQuestionIds(questionIds);
        quizRepository.save(quiz);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    private ResponseEntity<String> createQuizFallback(String quizTitle, int numQ, String category, Throwable throwable){
        return new ResponseEntity<>("FALLBACK", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuiz(Integer id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        List<QuestionWrapper> questionWrappers = new ArrayList<>();

        if(quiz.isPresent()){
            QuestionIds ids = new QuestionIds();
            ids.setQuestionIds(quiz.get().getQuestionIds());
            questionWrappers = quizInterface.getQuestionsByIds(ids).getBody();
        }
        return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> submitResult(Integer id, List<SubmitResult> submitResult) {
        return quizInterface.getScore(submitResult);
    }

    public ResponseEntity<List<Quiz>> getAllQuiz() {
        try {
            List<Quiz>  allQuiz = quizRepository.findAll();
            return new ResponseEntity<>(allQuiz, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }
}
