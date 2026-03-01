package com.lakshman.question_service.Service;

import com.lakshman.question_service.Utility.ResponseUtil;
import com.lakshman.question_service.Entity.Question;
import com.lakshman.question_service.Entity.SubmitResult;
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
import java.util.HashMap;
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
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("questionList", questionList);
        return ResponseUtil.ok("No of Questions: " + questionList.size(), responseData);
    }

    public ResponseEntity<?> getQuestionByCategory(String category) throws ResourceNotFoundException {

        Integer categoryId = categoryJpaRepository.findByCategoryName(category);
        Map<String, Object> responseData = new HashMap<>();

        if(categoryId != null){
            List<Question> questionList = questionJpaRepository.findByCategoryId(categoryId);
            responseData.put("categoryName", category);
            responseData.put("questionList", questionList);
            return ResponseUtil.ok("No of Questions: " + questionList.size() + " fetched from " + category,
                    responseData);
        }
        else throw new ResourceNotFoundException("Category Id is not found for " + category);
    }

    @Transactional
    public ResponseEntity<?> addQuestion(@Valid QuestionDTO questionDTO) {

        Map<String, Object> responseData = new HashMap<>();
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

            Question questionResponse = questionJpaRepository.save(question);
            responseData.put("Question Data", questionResponse);
            return ResponseUtil.created("Question add successfully", responseData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> addListOfQuestions(@Valid List<QuestionDTO> questionDTOs) {

        Map<String, Object> responseData = new HashMap<>();
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
            responseData.put("questionList", questions);
            return ResponseUtil.created("Question List added successfully. No of Question added: " + questions.size(), responseData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> generateQuestionIds(String category, Integer numQ) throws ResourceNotFoundException {
        Integer categoryId = categoryJpaRepository.findByCategoryName(category);
        List<Integer> questionIds;
        if(categoryId != null){
            Map<String, Object> responseData = new HashMap<>();
            questionIds = questionJpaRepository.getRandomQuestionIdsByCategory(categoryId, numQ);
            responseData.put("questionIds", questionIds);
            return ResponseUtil.ok("Random question Ids Generatd.", responseData);
        }
        throw new ResourceNotFoundException("Category Id is not found for " + category);
    }

    public ResponseEntity<?> getQuestionsByIds(List<Integer> questionIds) {
        Map<String, Object> responseData = new HashMap<>();
        List<Question> questionList  = new ArrayList<>();
        for(Integer id:questionIds){
            questionList.add(questionJpaRepository.findById(id).get());
        }
        List<QuestionWrapper> questionWrappers = questionList.stream().map(
                q-> new QuestionWrapper(q.getId(),
                q.getQuestion(), q.getOption1(),q.getOption2(), q.getOption3(),q.getOption4()))
                .collect(Collectors.toList());
        responseData.put("questionByIds", questionWrappers);
        return ResponseUtil.ok("Question found for " + questionIds, responseData);
    }

    public ResponseEntity<?> getScore(List<SubmitResult> submit) {
        Map<String, Object> responseData = new HashMap<>();
        int count = 0;
        for (SubmitResult result: submit) {
            Question questionDB = questionJpaRepository.findById(result.getId()).get();
            if(result.getResponse().equals(questionDB.getAnswer())){
                count++;
            }
        }
        responseData.put("score", count);
        log.info("Quiz Score: {}", count);
        return ResponseUtil.ok( "Received a score of " + count + "/" + submit.size(), responseData);
    }

    public ResponseEntity<?> getScoreV1(List<SubmitResult> submit) {
        Map<String, Object> responseData = new HashMap<>();
        List<Integer> ids = submit.stream().map(SubmitResult::getId).toList();
        List<SubmitResult> correctAns = questionJdbcRepository.getCorrectAnsByIds(ids);
        int count = 0;
        Map<Integer, String> correctAnswerMap = correctAns.stream().collect(
                Collectors.toMap(SubmitResult::getId, SubmitResult::getResponse)
        );
        for (SubmitResult s : submit) {
            String correctAnswer = correctAnswerMap.get(s.getId());
            if(s.getResponse().equals(correctAnswer)){
                count++;
            }
        }
        responseData.put("score", count);
        log.info("Quiz Score V1: {}", count);
        return ResponseUtil.ok( "Received a score of " + count + "/" + submit.size(), responseData);
    }

    public ResponseEntity<?> checkResult(List<Integer> questionIds) {
        Map<String, Object> responseData = new HashMap<>();
        List<SubmitResult> resultList = questionJdbcRepository.getCorrectAnsByIds(questionIds);
        responseData.put("correctAnswer", resultList);
        return ResponseUtil.ok("Review Answer", responseData);
    }


    public ResponseEntity<?> duplicateQuestions() {
        Map<String, Object> responseData = new HashMap<>();
        List<QuestionWrapper> duplicateQuestions = questionJdbcRepository.duplicateData();
        if(!CollectionUtils.isEmpty(duplicateQuestions)){
            responseData.put("duplicate", duplicateQuestions);
            return ResponseUtil.ok("Duplicate Questions found: " + duplicateQuestions.size(), responseData);
        }
        else return ResponseUtil.noContent("No Duplicates", responseData);
    }

    @Transactional
    public ResponseEntity<?> deleteDuplicates() {
        Map<String, Object> responseData = new HashMap<>();
        try{
            Integer rows =  questionJpaRepository.deleteDuplicates();
            responseData.put("count", rows);
            return ResponseUtil.ok( rows +  " duplicate Questions has been deleted successfully", responseData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
