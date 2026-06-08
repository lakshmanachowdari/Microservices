package com.lakshman.question_service.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lakshman.question_service.Entity.TestResponse;
import com.lakshman.question_service.Service.QuestionService;
import com.lakshman.question_service.Wrapper.QuestionDTO;
import com.lakshman.question_service.Wrapper.QuestionIds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
@DisplayName("QuestionController MVC tests")
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    private QuestionDTO sampleDto;
    private List<QuestionDTO> sampleList;
    private QuestionIds sampleIds;
    private TestResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleDto = new QuestionDTO();
        sampleDto.setQuestion("What is 2+2?");
        sampleDto.setOption1("1");
        sampleDto.setOption2("2");
        sampleDto.setOption3("3");
        sampleDto.setOption4("4");
        sampleDto.setAnswer("4");
        sampleDto.setQuestionLevel("EASY");
        sampleDto.setCategoryName("Math");

        sampleList = Collections.singletonList(sampleDto);

        sampleIds = new QuestionIds();
        sampleIds.setQuestionIds(Arrays.asList(1, 2, 3));

        sampleResponse = new TestResponse(1, "4");
    }

    @Test
    void getAllQuestions_returnsOkAndLogsPort() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("data", Collections.emptyList());

        ResponseEntity<?> okResp = ResponseEntity.ok(body);
        doReturn(okResp).when(questionService).getAllQuestions();

        mockMvc.perform(get("/question/allQuestions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true));

        verify(questionService, times(1)).getAllQuestions();
    }

    @Test
    void getQuestionByCategory_success() throws Exception {
        String category = "Math";
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("data", sampleList);

        ResponseEntity<?> okResp = ResponseEntity.ok(body);
        doReturn(okResp).when(questionService).getQuestionByCategory(category);

        mockMvc.perform(get("/question/category/{category}", category)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].question").value(sampleDto.getQuestion()));

        verify(questionService, times(1)).getQuestionByCategory(category);
    }

    @Test
    void addQuestion_success() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("message", "Question added");

        ResponseEntity<?> created = ResponseEntity.status(HttpStatus.CREATED).body(body);
        doReturn(created).when(questionService).addQuestion(any(QuestionDTO.class));

        mockMvc.perform(post("/question/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Question added"));

        verify(questionService, times(1)).addQuestion(any(QuestionDTO.class));
    }

    @Test
    void addListOfQuestions_success() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("count", sampleList.size());

        ResponseEntity<?> created = ResponseEntity.status(HttpStatus.CREATED).body(body);
        doReturn(created).when(questionService).addListOfQuestions(anyList());

        mockMvc.perform(post("/question/addList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleList)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.count").value(sampleList.size()));

        verify(questionService, times(1)).addListOfQuestions(anyList());
    }

    @Test
    void generateQuestionIds_success() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("questionIds", Arrays.asList(1, 2, 3));

        ResponseEntity<?> okResp = ResponseEntity.ok(body);
        doReturn(okResp).when(questionService).generateQuestionIds(eq("Math"), eq(3));

        mockMvc.perform(get("/question/generate")
                        .param("category", "Math")
                        .param("numQ", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionIds").isArray());

        verify(questionService, times(1)).generateQuestionIds("Math", 3);
    }

    @Test
    void getQuestionsByIds_success() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("data", Collections.emptyList());

        ResponseEntity<?> okResp = ResponseEntity.ok(body);
        doReturn(okResp).when(questionService).getQuestionsByIds(anyList());

        mockMvc.perform(post("/question/getQuestions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(questionService, times(1)).getQuestionsByIds(anyList());
    }

    @Test
    void getScore_success() throws Exception {
        List<TestResponse> submit = Arrays.asList(sampleResponse);
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("score", 1);

        ResponseEntity<?> okResp = ResponseEntity.ok(body);
        doReturn(okResp).when(questionService).getScore(anyList());

        mockMvc.perform(post("/question/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(1));

        verify(questionService, times(1)).getScore(anyList());
    }

    @Test
    void getScoreV1_success() throws Exception {
        List<TestResponse> submit = Arrays.asList(sampleResponse);
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("score", 1);

        ResponseEntity<?> okResp = ResponseEntity.ok(body);
        doReturn(okResp).when(questionService).getScoreV1(anyList());

        mockMvc.perform(post("/question/v1/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(1));

        verify(questionService, times(1)).getScoreV1(anyList());
    }

    @Test
    void checkResult_success() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("results", Arrays.asList(1, 2));

        ResponseEntity<?> okResp = ResponseEntity.ok(body);
        doReturn(okResp).when(questionService).checkResult(anyList());

        mockMvc.perform(get("/question/check")
                        .param("ids", "1", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray());

        verify(questionService, times(1)).checkResult(anyList());
    }

    @Test
    void duplicateQuestions_success() throws Exception {
        Map<String, Object> dupBody = new HashMap<>();
        dupBody.put("success", true);
        dupBody.put("duplicates", Arrays.asList(1, 2));

        ResponseEntity<?> dupResp = ResponseEntity.ok(dupBody);
        doReturn(dupResp).when(questionService).duplicateQuestions();

        mockMvc.perform(get("/question/duplicateData")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duplicates").isArray());

        verify(questionService, times(1)).duplicateQuestions();
    }

    @Test
    void deleteDuplicates_success() throws Exception {
        Map<String, Object> delBody = new HashMap<>();
        delBody.put("success", true);
        delBody.put("deletedCount", 2);

        ResponseEntity<?> delResp = ResponseEntity.ok(delBody);
        doReturn(delResp).when(questionService).deleteDuplicates();

        mockMvc.perform(put("/question/duplicate/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deletedCount").value(2));

        verify(questionService, times(1)).deleteDuplicates();
    }

    @Test
    void getResponseByIds_success() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("data", Collections.emptyList());

        ResponseEntity<?> okResp = ResponseEntity.ok(body);
        doReturn(okResp).when(questionService).getResponsesByIds(anyList());

        mockMvc.perform(post("/question/response")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(questionService, times(1)).getResponsesByIds(anyList());
    }
}