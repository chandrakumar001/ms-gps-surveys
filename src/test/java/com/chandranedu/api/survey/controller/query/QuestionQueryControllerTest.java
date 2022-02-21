package com.chandranedu.api.survey.controller.query;

import com.chandranedu.api.survey.service.QuestionQueryService;
import com.chandranedu.api.swagger.model.question.QuestionDTO;
import com.chandranedu.api.swagger.model.question.QuestionsResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.chandranedu.api.survey.ObjectMapperUtil.asJsonString;
import static com.chandranedu.api.survey.mockdata.QuestionMockData.getQuestionDTO;
import static com.chandranedu.api.survey.mockdata.QuestionMockData.getQuestionsResponseDTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionQueryController.class)
class QuestionQueryControllerTest {

    @MockBean
    private QuestionQueryService questionQueryService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testQuestionQueryController_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new QuestionQueryController(null);
        });
        // then
        assertTrue(exception.getMessage().contains("questionQueryService is marked non-null but is null"));
    }

    @Test
    void testGetQuestionById_success() throws Exception {
        //given
        final String questionTitle = "sample question title?";
        final String answerText = "sample answer text";
        final Boolean isCorrectAnswer = true;
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();

        final QuestionDTO questionDTO = getQuestionDTO(questionTitle, answerText, isCorrectAnswer);
        when(questionQueryService.getQuestionById(reqQuestionId))
                .thenReturn(questionDTO);
        //expected
        this.mockMvc.perform(get("/v1/questions/" + reqQuestionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(questionDTO)));
    }

    @Test
    void testGetQuestions_success() throws Exception {
        //given
        final String questionTitle = "sample question title?";
        final String answerText = "sample answer text";
        final Boolean isCorrectAnswer = true;

        final QuestionsResponseDTO questionDTO = getQuestionsResponseDTO(questionTitle, answerText, isCorrectAnswer);
        when(questionQueryService.getQuestions()).thenReturn(questionDTO);
        //expected
        this.mockMvc.perform(get("/v1/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(questionDTO)));

    }
}
