package com.lakshman.question_service.Service;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionService {

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Autowired
    QuestionJdbcRepository questionJdbcRepository;

    @Autowired
    CategoryJpaRepository categoryJpaRepository;

    @Autowired
    CategoryJdbcRepository categoryJdbcRepository;

    public List<Question> getAllQuestions() {
        return questionJpaRepository.findAll();
    }

    public List<Question> getQuestionByCategory(String category) throws ResourceNotFoundException {

        Integer categoryId = categoryJpaRepository.findByCategoryName(category);
        if(categoryId != null)
            return questionJpaRepository.findByCategoryId(categoryId);
        else throw new ResourceNotFoundException("Category Id is not found for " + category);
    }

    @Transactional
    public String addQuestion(QuestionDTO questionDTO) {
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

            questionJpaRepository.save(question);
            return "SUCCESS";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public String addListOfQuestions(List<QuestionDTO> questionDTOs) {
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
            questionJpaRepository.saveAll(questionList);
            return "SUCCESS";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> generateQuestionIds(String category, Integer numQ) throws ResourceNotFoundException {
        Integer categoryId = categoryJpaRepository.findByCategoryName(category);
        if(categoryId != null)
            return questionJpaRepository.getRandomQuestionIdsByCategory(categoryId, numQ);
        else throw new ResourceNotFoundException("Category Id is not found for " + category);
    }

    public List<QuestionWrapper> getQuestionsByIds(List<Integer> questionIds) {
        List<Question> questionList  = new ArrayList<>();
        for(Integer id:questionIds){
            questionList.add(questionJpaRepository.findById(id).get());
        }

        return questionList.stream().map(
                q-> new QuestionWrapper(q.getId(),
                q.getQuestion(), q.getOption1(),q.getOption2(), q.getOption3(),q.getOption4()))
                .collect(Collectors.toList());
    }

    public Integer getScore(List<SubmitResult> submit) {
        int count = 0;
        for (SubmitResult result: submit) {
            Question questionDB = questionJpaRepository.findById(result.getId()).get();
            if(result.getResponse().equals(questionDB.getAnswer())){
                count++;
            }
        }
        System.out.print("Quiz Score: ");
        return count;
    }

    public Integer getScoreV1(List<SubmitResult> submit) {
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
        System.out.print("Quiz Score: ");
        return count;
    }

    public List<SubmitResult> checkResult(List<Integer> questionIds) {
        return questionJdbcRepository.getCorrectAnsByIds(questionIds);

    }


    public List<QuestionWrapper> duplicateQuestions() {
        return questionJdbcRepository.duplicateData();
    }

    @Transactional
    public String deleteDuplicates() {
        try{
            Integer rows =  questionJpaRepository.deleteDuplicates();
            return "Number of rows deleted: " +rows;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
