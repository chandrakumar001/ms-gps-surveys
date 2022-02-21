package com.chandranedu.api.survey.controller.command;

import com.chandranedu.api.error.exception.FieldValidationException;
import com.chandranedu.api.survey.service.QuestionCommandService;
import com.chandranedu.api.swagger.model.question.QuestionDTO;
import com.chandranedu.api.swagger.model.question.QuestionRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.chandranedu.api.survey.ObjectMapperUtil.asJsonString;
import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT;
import static com.chandranedu.api.survey.mockdata.QuestionMockData.getQuestionDTO;
import static com.chandranedu.api.survey.mockdata.QuestionMockData.getQuestionResponseDTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionCommandController.class)
class QuestionCommandControllerTest {

    @MockBean
    private QuestionCommandService questionCommandService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testQuestionCommandController_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new QuestionCommandController(null);
        });
        // then
        assertTrue(exception.getMessage().contains("questionCommandService is marked non-null but is null"));
    }

    @Test
    void testCreateSurvey() throws Exception {
        //given
        final String questionTitle = "what is current year?";
        final String answerText = "2022";
        final Boolean isCorrectAnswer = true;

        final QuestionRequestDTO questionRequestDTO = getQuestionResponseDTO(
                questionTitle,
                answerText,
                isCorrectAnswer
        );

        final QuestionDTO questionDTO = getQuestionDTO(questionTitle, answerText, isCorrectAnswer);
        when(questionCommandService.createQuestion(questionRequestDTO))
                .thenReturn(questionDTO);
        //expected
        mockMvc.perform(post("/v1/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(questionRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(questionDTO)));
    }


    @Test
    void testDeleteSurveyById_success() throws Exception {
        //given
        final String reqQuestionId = UUID.randomUUID().toString();

        doNothing().when(questionCommandService).deleteQuestionById(reqQuestionId);
        //expected
        this.mockMvc.perform(delete("/v1/questions/" + reqQuestionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteSurveyById_InvalidSurveyId() throws Exception {
        //given
        final String reqQuestionId = "3c7818e1-d0ac-4b4a-a402";

        doThrow(new FieldValidationException(ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT))
                .when(questionCommandService).deleteQuestionById(reqQuestionId);
        //expected
        this.mockMvc.perform(delete("/v1/questions/" + reqQuestionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
