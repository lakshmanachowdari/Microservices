package com.lakshman.question_service.Service;

import com.lakshman.question_service.Entity.TestResponse;
import com.lakshman.question_service.Utility.ResponseUtil;
import com.lakshman.question_service.Entity.Question;
import com.lakshman.question_service.Exception.ResourceNotFoundException;
import com.lakshman.question_service.Repository.CategoryJdbcRepository;
import com.lakshman.question_service.Repository.CategoryJpaRepository;
import com.lakshman.question_service.Repository.QuestionJdbcRepository;
import com.lakshman.question_service.Repository.QuestionJpaRepository;
import com.lakshman.question_service.Wrapper.QuestionDTO;
import com.lakshman.question_service.Wrapper.QuestionWrapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionService {

    @Autowired
    private QuestionJpaRepository questionJpaRepository;

    @Autowired
    private QuestionJdbcRepository questionJdbcRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private CategoryJdbcRepository categoryJdbcRepository;

    public ResponseEntity<?> getAllQuestions() {
        List<Question> questionList = questionJpaRepository.findAll();
        return ResponseUtil.ok("No of Questions: " + questionList.size(), questionList);
    }

    public ResponseEntity<?> getQuestionByCategory(String category) throws ResourceNotFoundException {

        Integer categoryId = categoryJpaRepository.findByCategoryName(category);
        if(categoryId != null){
            List<Question> questionList = questionJpaRepository.findByCategoryId(categoryId);
            return ResponseUtil.ok("No of Questions: " + questionList.size() + " fetched from " + category,
                    questionList);
        }
        else throw new ResourceNotFoundException("Category Id is not found for " + category);
    }

    @Transactional
    public ResponseEntity<?> addQuestion(@Valid QuestionDTO questionDTO) {

        try {
            Question question = new Question();
            question.setQuestion(questionDTO.getQuestion());
            question.setOption1(questionDTO.getOption1());
            question.setOption2(questionDTO.getOption2());
            question.setOption3(questionDTO.getOption3());
            question.setOption4(questionDTO.getOption4());
            question.setAnswer(questionDTO.getAnswer());
            question.setQuestionLevel(questionDTO.getQuestionLevel());

            Integer categoryId = categoryJpaRepository.findByCategoryName(questionDTO.getCategoryName());
            if(categoryId != null)
                question.setCategoryId(categoryId);
            else throw new ResourceNotFoundException("Category Id is not found for " + questionDTO.getCategoryName());

            return ResponseUtil.created("Question add successfully", questionJpaRepository.save(question));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> addListOfQuestions(@Valid List<QuestionDTO> questionDTOs) {

        try {
            Set<String> categories = new HashSet<>();
            for(QuestionDTO category : questionDTOs)
                categories.add(category.getCategoryName());
            Map<String, Integer> categoryMap = categoryJdbcRepository.findAllByCategoryName(categories);

            List<Question> questionList = new ArrayList<>();
            for(QuestionDTO questionDTO : questionDTOs){
                Question question = new Question();
                question.setQuestion(questionDTO.getQuestion());
                question.setOption1(questionDTO.getOption1());
                question.setOption2(questionDTO.getOption2());
                question.setOption3(questionDTO.getOption3());
                question.setOption4(questionDTO.getOption4());
                question.setAnswer(questionDTO.getAnswer());
                question.setQuestionLevel(questionDTO.getQuestionLevel());
                if (categoryMap.containsKey(questionDTO.getCategoryName())){
                    question.setCategoryId(categoryMap.get(questionDTO.getCategoryName()));
                }
                else throw new ResourceNotFoundException("Category Id is not found for " + questionDTO.getCategoryName());
                questionList.add(question);
            }
            List<Question> questions = questionJpaRepository.saveAll(questionList);
            return ResponseUtil.created("Question List added successfully. No of Question added: " + questions.size(), questions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> generateQuestionIds(String category, Integer numQ) throws ResourceNotFoundException {
        Integer categoryId = categoryJpaRepository.findByCategoryName(category);
        List<Integer> questionIds;
        if(categoryId != null){
            questionIds = questionJpaRepository.getRandomQuestionIdsByCategory(categoryId, numQ);
            return ResponseUtil.ok("Random question Ids Generated.", questionIds);
        }
        throw new ResourceNotFoundException("Category Id is not found for " + category);
    }

    public ResponseEntity<?> getQuestionsByIds(List<Integer> questionIds) {

        List<Question> questionList  =questionJpaRepository.findAllById(questionIds);

        List<QuestionWrapper> questionWrappers = questionList.stream().map(
                q-> new QuestionWrapper(q.getId(),
                q.getQuestion(), q.getOption1(),q.getOption2(), q.getOption3(),q.getOption4()))
                .collect(Collectors.toList());

        return ResponseUtil.ok("Question found for " + questionIds, questionWrappers);
    }

    public ResponseEntity<?> getScore(List<TestResponse> submit) {
        int count = 0;
        for (TestResponse result: submit) {
            Question questionDB = questionJpaRepository.findById(result.getId()).orElseThrow();
            if(result.getResponse().equals(questionDB.getAnswer())){
                count++;
            }
        }

        log.info("Quiz Score: {}", count);
        return ResponseUtil.ok( "Received a score of " + count + "/" + submit.size(), count);
    }

    public ResponseEntity<?> getScoreV1(List<TestResponse> submit) {
        List<Integer> ids = submit.stream().map(TestResponse::getId).toList();
        List<TestResponse> correctAns = questionJdbcRepository.getCorrectAnsByIds(ids);
        int count = 0;
        Map<Integer, String> correctAnswerMap = correctAns.stream().collect(
                Collectors.toMap(TestResponse::getId, TestResponse::getResponse)
        );
        for (TestResponse s : submit) {
            String correctAnswer = correctAnswerMap.get(s.getId());
            if(s.getResponse().equals(correctAnswer)){
                count++;
            }
        }
        log.info("Quiz Score V1: {}", count);
        return ResponseUtil.ok( "Received a score of " + count + "/" + submit.size(), count);
    }

    public ResponseEntity<?> checkResult(List<Integer> questionIds) {
        return ResponseUtil.ok("Review Answer", questionJdbcRepository.getCorrectAnsByIds(questionIds));
    }

    public ResponseEntity<?> duplicateQuestions() {
        List<QuestionWrapper> duplicateQuestions = questionJdbcRepository.duplicateData();
        if(!CollectionUtils.isEmpty(duplicateQuestions)){
            return ResponseUtil.ok("Duplicate Questions found: " + duplicateQuestions.size(), duplicateQuestions);
        }
        else return ResponseUtil.ok("No duplicates found", duplicateQuestions.size());
    }

    @Transactional
    public ResponseEntity<?> deleteDuplicates() {
        try{
            Integer rows =  questionJpaRepository.deleteDuplicates();
            return ResponseUtil.ok( rows +  " duplicate Questions has been deleted successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> getResponsesByIds(List<Integer> questionIds) {
        List<Question> questionList  =questionJpaRepository.findAllById(questionIds);
        List<TestResponse> response = questionList.stream().map(
                res -> new TestResponse(res.getId(), res.getAnswer())).toList();
        return ResponseUtil.ok("Test Response", response);
    }
}
