package com.lakshman.question_service.Service;

import com.lakshman.question_service.Entity.Question;
import com.lakshman.question_service.Entity.TestResponse;
import com.lakshman.question_service.Exception.ResourceNotFoundException;
import com.lakshman.question_service.Repository.CategoryJdbcRepository;
import com.lakshman.question_service.Repository.CategoryJpaRepository;
import com.lakshman.question_service.Repository.QuestionJdbcRepository;
import com.lakshman.question_service.Repository.QuestionJpaRepository;
import com.lakshman.question_service.Wrapper.QuestionDTO;
import com.lakshman.question_service.Wrapper.QuestionWrapper;
import com.lakshman.question_service.Utility.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuestionService Unit Tests")
class QuestionServiceTest {

    @Mock
    private QuestionJpaRepository questionJpaRepository;

    @Mock
    private QuestionJdbcRepository questionJdbcRepository;

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

    @Mock
    private CategoryJdbcRepository categoryJdbcRepository;

    @InjectMocks
    private QuestionService questionService;

    private Question sampleQuestion;

    @BeforeEach
    void setUp() {
        sampleQuestion = new Question();
        sampleQuestion.setId(1);
        sampleQuestion.setQuestion("What is the capital of France?");
        sampleQuestion.setOption1("London");
        sampleQuestion.setOption2("Paris");
        sampleQuestion.setOption3("Berlin");
        sampleQuestion.setOption4("Madrid");
        sampleQuestion.setAnswer("Paris");
        sampleQuestion.setQuestionLevel("MEDIUM");
        sampleQuestion.setCategoryId(2);
    }

    @Test
    void getAllQuestions_returnsList() {
        List<Question> list = Arrays.asList(sampleQuestion);
        when(questionJpaRepository.findAll()).thenReturn(list);

        ResponseEntity<?> resp = questionService.getAllQuestions();
        assertEquals(200, resp.getStatusCodeValue());

        @SuppressWarnings("unchecked")
        ApiResponse<List<Question>> body = (ApiResponse<List<Question>>) resp.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals(1, body.getData().size());

        verify(questionJpaRepository, times(1)).findAll();
    }

    @Test
    void getQuestionByCategory_success() throws Exception {
        String cat = "Geography";
        when(categoryJpaRepository.findByCategoryName(cat)).thenReturn(2);
        when(questionJpaRepository.findByCategoryId(2)).thenReturn(Arrays.asList(sampleQuestion));

        ResponseEntity<?> resp = questionService.getQuestionByCategory(cat);

        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        ApiResponse<List<Question>> body = (ApiResponse<List<Question>>) resp.getBody();
        assertEquals(1, body.getData().size());

        verify(categoryJpaRepository, times(1)).findByCategoryName(cat);
        verify(questionJpaRepository, times(1)).findByCategoryId(2);
    }

    @Test
    void getQuestionByCategory_notFound_throws() {
        String cat = "Unknown";
        when(categoryJpaRepository.findByCategoryName(cat)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> questionService.getQuestionByCategory(cat));
        verify(categoryJpaRepository, times(1)).findByCategoryName(cat);
        verifyNoMoreInteractions(questionJpaRepository);
    }

    @Test
    void addQuestion_success() {
        QuestionDTO dto = new QuestionDTO();
        dto.setQuestion(sampleQuestion.getQuestion());
        dto.setOption1(sampleQuestion.getOption1());
        dto.setOption2(sampleQuestion.getOption2());
        dto.setOption3(sampleQuestion.getOption3());
        dto.setOption4(sampleQuestion.getOption4());
        dto.setAnswer(sampleQuestion.getAnswer());
        dto.setQuestionLevel(sampleQuestion.getQuestionLevel());
        dto.setCategoryName("Geography");

        when(categoryJpaRepository.findByCategoryName("Geography")).thenReturn(2);
        Question saved = new Question();
        saved.setId(10);
        when(questionJpaRepository.save(any(Question.class))).thenReturn(saved);

        ResponseEntity<?> resp = questionService.addQuestion(dto);
        assertEquals(201, resp.getStatusCodeValue());

        @SuppressWarnings("unchecked")
        ApiResponse<Question> body = (ApiResponse<Question>) resp.getBody();
        assertNotNull(body.getData());
        assertEquals(10, body.getData().getId());

        ArgumentCaptor<Question> captor = ArgumentCaptor.forClass(Question.class);
        verify(questionJpaRepository).save(captor.capture());
        assertEquals(2, captor.getValue().getCategoryId());
    }

    @Test
    void addQuestion_categoryMissing_throws() {
        QuestionDTO dto = new QuestionDTO();
        dto.setQuestion(sampleQuestion.getQuestion());
        dto.setOption1(sampleQuestion.getOption1());
        dto.setOption2(sampleQuestion.getOption2());
        dto.setOption3(sampleQuestion.getOption3());
        dto.setOption4(sampleQuestion.getOption4());
        dto.setAnswer(sampleQuestion.getAnswer());
        dto.setQuestionLevel(sampleQuestion.getQuestionLevel());
        dto.setCategoryName("MissingCat");

        when(categoryJpaRepository.findByCategoryName("MissingCat")).thenReturn(null);

        // Service wraps the ResourceNotFoundException in a RuntimeException
        RuntimeException ex = assertThrows(RuntimeException.class, () -> questionService.addQuestion(dto));

        assertNotNull(ex.getCause(), "Expected a cause for the RuntimeException");
        assertTrue(ex.getCause() instanceof ResourceNotFoundException,
                "Expected cause to be ResourceNotFoundException but was: " + ex.getCause().getClass());

        verify(categoryJpaRepository, times(1)).findByCategoryName("MissingCat");
        verifyNoInteractions(questionJpaRepository);
    }

    @Test
    void addListOfQuestions_success() {
        QuestionDTO dto1 = new QuestionDTO();
        dto1.setQuestion("Q1");
        dto1.setOption1("a");
        dto1.setOption2("b");
        dto1.setOption3("c");
        dto1.setOption4("d");
        dto1.setAnswer("a");
        dto1.setQuestionLevel("EASY");
        dto1.setCategoryName("CatA");

        QuestionDTO dto2 = new QuestionDTO();
        dto2.setQuestion("Q2");
        dto2.setOption1("a");
        dto2.setOption2("b");
        dto2.setOption3("c");
        dto2.setOption4("d");
        dto2.setAnswer("b");
        dto2.setQuestionLevel("EASY");
        dto2.setCategoryName("CatB");

        List<QuestionDTO> dtos = Arrays.asList(dto1, dto2);

        Map<String, Integer> catMap = new HashMap<>();
        catMap.put("CatA", 11);
        catMap.put("CatB", 12);

        when(categoryJdbcRepository.findAllByCategoryName(anySet())).thenReturn(catMap);

        Question q1 = new Question(); q1.setId(1);
        Question q2 = new Question(); q2.setId(2);
        when(questionJpaRepository.saveAll(anyList())).thenReturn(Arrays.asList(q1, q2));

        ResponseEntity<?> resp = questionService.addListOfQuestions(dtos);
        assertEquals(201, resp.getStatusCodeValue());

        @SuppressWarnings("unchecked")
        ApiResponse<List<Question>> body = (ApiResponse<List<Question>>) resp.getBody();
        assertEquals(2, body.getData().size());

        verify(categoryJdbcRepository, times(1)).findAllByCategoryName(anySet());
        verify(questionJpaRepository, times(1)).saveAll(anyList());
    }

    @Test
    void addListOfQuestions_missingCategory_throws() {
        QuestionDTO dto1 = new QuestionDTO();
        dto1.setQuestion("Q1");
        dto1.setOption1("a");
        dto1.setOption2("b");
        dto1.setOption3("c");
        dto1.setOption4("d");
        dto1.setAnswer("a");
        dto1.setQuestionLevel("EASY");
        dto1.setCategoryName("CatA");

        List<QuestionDTO> dtos = Collections.singletonList(dto1);

        // categoryJdbcRepository returns empty map -> missing category
        when(categoryJdbcRepository.findAllByCategoryName(anySet())).thenReturn(Collections.emptyMap());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> questionService.addListOfQuestions(dtos));

        // verify the original cause is ResourceNotFoundException
        assertNotNull(ex.getCause());
        assertTrue(ex.getCause() instanceof ResourceNotFoundException,
                "Expected cause to be ResourceNotFoundException but was: " + ex.getCause().getClass());

        verify(categoryJdbcRepository, times(1)).findAllByCategoryName(anySet());
        verifyNoInteractions(questionJpaRepository);
    }

    @Test
    void generateQuestionIds_success() {
        when(categoryJpaRepository.findByCategoryName("Geography")).thenReturn(2);
        when(questionJpaRepository.getRandomQuestionIdsByCategory(2, 3)).thenReturn(Arrays.asList(5, 6, 7));

        ResponseEntity<?> resp = questionService.generateQuestionIds("Geography", 3);
        assertEquals(200, resp.getStatusCodeValue());

        @SuppressWarnings("unchecked")
        ApiResponse<List<Integer>> body = (ApiResponse<List<Integer>>) resp.getBody();
        assertEquals(Arrays.asList(5,6,7), body.getData());
    }

    @Test
    void generateQuestionIds_notFound_throws() {
        when(categoryJpaRepository.findByCategoryName("NoCat")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> questionService.generateQuestionIds("NoCat", 2));
    }

    @Test
    void getQuestionsByIds_returnsWrapped() {
        Question q = sampleQuestion;
        when(questionJpaRepository.findAllById(Arrays.asList(1, 2))).thenReturn(Arrays.asList(q));

        ResponseEntity<?> resp = questionService.getQuestionsByIds(Arrays.asList(1, 2));
        assertEquals(200, resp.getStatusCodeValue());

        @SuppressWarnings("unchecked")
        ApiResponse<List<QuestionWrapper>> body = (ApiResponse<List<QuestionWrapper>>) resp.getBody();
        assertEquals(1, body.getData().size());
        assertEquals(q.getQuestion(), body.getData().get(0).getQuestion());
    }

    @Test
    void getScore_countsCorrectAnswers() {
        TestResponse r1 = new TestResponse(1, "Paris");
        TestResponse r2 = new TestResponse(2, "4");

        Question q1 = new Question();
        q1.setId(1);
        q1.setAnswer("Paris");

        Question q2 = new Question();
        q2.setId(2);
        q2.setAnswer("4");

        when(questionJpaRepository.findById(1)).thenReturn(Optional.of(q1));
        when(questionJpaRepository.findById(2)).thenReturn(Optional.of(q2));

        ResponseEntity<?> resp = questionService.getScore(Arrays.asList(r1, r2));
        assertEquals(200, resp.getStatusCodeValue());

        @SuppressWarnings("unchecked")
        ApiResponse<Integer> body = (ApiResponse<Integer>) resp.getBody();
        assertEquals(2, body.getData().intValue());
    }

    @Test
    void getScore_partial() {
        TestResponse r1 = new TestResponse(1, "Paris");
        TestResponse r2 = new TestResponse(2, "wrong");

        Question q1 = new Question();
        q1.setId(1);
        q1.setAnswer("Paris");

        Question q2 = new Question();
        q2.setId(2);
        q2.setAnswer("4");

        when(questionJpaRepository.findById(1)).thenReturn(Optional.of(q1));
        when(questionJpaRepository.findById(2)).thenReturn(Optional.of(q2));

        ResponseEntity<?> resp = questionService.getScore(Arrays.asList(r1, r2));
        @SuppressWarnings("unchecked")
        ApiResponse<Integer> body = (ApiResponse<Integer>) resp.getBody();
        assertEquals(1, body.getData().intValue());
    }

    @Test
    void getScoreV1_usesJdbcRepo() {
        TestResponse r1 = new TestResponse(1, "Paris");
        TestResponse r2 = new TestResponse(2, "4");
        List<TestResponse> submit = Arrays.asList(r1, r2);

        // simulate correct answers returned by jdbc repo
        when(questionJdbcRepository.getCorrectAnsByIds(Arrays.asList(1,2)))
                .thenReturn(Arrays.asList(new TestResponse(1, "Paris"), new TestResponse(2, "4")));

        ResponseEntity<?> resp = questionService.getScoreV1(submit);
        @SuppressWarnings("unchecked")
        ApiResponse<Integer> body = (ApiResponse<Integer>) resp.getBody();
        assertEquals(2, body.getData().intValue());

        verify(questionJdbcRepository, times(1)).getCorrectAnsByIds(Arrays.asList(1,2));
    }

    @Test
    void checkResult_returnsCorrectAnswers() {
        when(questionJdbcRepository.getCorrectAnsByIds(Arrays.asList(1,2)))
                .thenReturn(Arrays.asList(new TestResponse(1, "Paris"), new TestResponse(2, "4")));

        ResponseEntity<?> resp = questionService.checkResult(Arrays.asList(1,2));
        @SuppressWarnings("unchecked")
        ApiResponse<List<TestResponse>> body = (ApiResponse<List<TestResponse>>) resp.getBody();
        assertEquals(2, body.getData().size());
        verify(questionJdbcRepository, times(1)).getCorrectAnsByIds(Arrays.asList(1,2));
    }

    @Test
    void duplicateQuestions_foundAndEmpty() {
        QuestionWrapper w = new QuestionWrapper(1, "Q", "a","b","c","d");
        when(questionJdbcRepository.duplicateData()).thenReturn(Arrays.asList(w));
        ResponseEntity<?> resp = questionService.duplicateQuestions();
        @SuppressWarnings("unchecked")
        ApiResponse<List<QuestionWrapper>> body = (ApiResponse<List<QuestionWrapper>>) resp.getBody();
        assertEquals(1, body.getData().size());

        // when empty
        when(questionJdbcRepository.duplicateData()).thenReturn(emptyList());
        ResponseEntity<?> resp2 = questionService.duplicateQuestions();
        @SuppressWarnings("unchecked")
        ApiResponse<Integer> body2 = (ApiResponse<Integer>) resp2.getBody();
        assertEquals(0, body2.getData().intValue());
    }

    @Test
    void deleteDuplicates_success() {
        when(questionJpaRepository.deleteDuplicates()).thenReturn(5);
        ResponseEntity<?> resp = questionService.deleteDuplicates();
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        ApiResponse<String> body = (ApiResponse<String>) resp.getBody();
        assertTrue(body.getMessage().contains("duplicate Questions"));
        verify(questionJpaRepository, times(1)).deleteDuplicates();
    }

    @Test
    void getResponsesByIds_returnsAnswers() {
        Question q1 = new Question(); q1.setId(1); q1.setAnswer("Paris");
        Question q2 = new Question(); q2.setId(2); q2.setAnswer("4");
        when(questionJpaRepository.findAllById(Arrays.asList(1,2))).thenReturn(Arrays.asList(q1,q2));

        ResponseEntity<?> resp = questionService.getResponsesByIds(Arrays.asList(1,2));
        @SuppressWarnings("unchecked")
        ApiResponse<List<TestResponse>> body = (ApiResponse<List<TestResponse>>) resp.getBody();
        assertEquals(2, body.getData().size());
    }
}