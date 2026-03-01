package com.lakshman.question_service.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lakshman.question_service.Entity.Question;
import com.lakshman.question_service.MockData.DataUtils;
import com.lakshman.question_service.Repository.QuestionJpaRepository;
import com.lakshman.question_service.Service.QuestionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    private final DataUtils dataUtils = new DataUtils();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    QuestionService questionService;

    @Test
    void testGetAllQuestionsSuccess() throws Exception{

        mockMvc.perform(get("/question/allQuestions")   // base path + constant
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].question").value("What is Java?"))
                .andExpect(jsonPath("$[1].question").value("What is Spring Boot?"));

        Mockito.verify(questionService, Mockito.times(1)).getAllQuestions();
    }
}
