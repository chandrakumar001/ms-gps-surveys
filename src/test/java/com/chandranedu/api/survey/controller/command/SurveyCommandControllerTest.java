package com.chandranedu.api.survey.controller.command;

import com.chandranedu.api.error.exception.FieldValidationException;
import com.chandranedu.api.survey.service.SurveyCommandService;
import com.chandranedu.api.swagger.model.survey.SurveyDTO;
import com.chandranedu.api.swagger.model.survey.SurveyRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.chandranedu.api.survey.ObjectMapperUtil.asJsonString;
import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT;
import static com.chandranedu.api.survey.mockdata.SurveyMockData.getSurveyDTO;
import static com.chandranedu.api.survey.mockdata.SurveyMockData.getSurveyRequestDTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SurveyCommandController.class)
class SurveyCommandControllerTest {

    @MockBean
    private SurveyCommandService surveyCommandService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSurveyCommandController_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new SurveyCommandController(null);
        });
        // then
        assertTrue(exception.getMessage().contains("surveyCommandService is marked non-null but is null"));
    }

    @Test
    void testCreateSurvey() throws Exception {
        //given
        final String surveyTitle = "what is survey?";
        final String active = "ACTIVE";
        final UUID surveyId = UUID.randomUUID();

        final SurveyRequestDTO surveyRequestDTO = getSurveyRequestDTO(
                surveyTitle,
                active
        );

        final SurveyDTO surveyDTO = getSurveyDTO(surveyTitle, active,surveyId);
        when(surveyCommandService.createSurvey(surveyRequestDTO))
                .thenReturn(surveyDTO);
        //expected
        mockMvc.perform(post("/v1/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(surveyRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(surveyDTO)));
    }

    @Test
    void testDeleteSurveyById_success() throws Exception {
        //given
        final String reqSurveyId = UUID.randomUUID().toString();

        doNothing().when(surveyCommandService).deleteSurveyById(reqSurveyId);
        //expected
        this.mockMvc.perform(delete("/v1/surveys/" + reqSurveyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteSurveyById_InvalidSurveyId() throws Exception {
        //given
        final String reqSurveyId = "3c7818e1-d0ac-4b4a-a402";

        doThrow(new FieldValidationException(ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT))
                .when(surveyCommandService).deleteSurveyById(reqSurveyId);
        //expected
        this.mockMvc.perform(delete("/v1/surveys/" + reqSurveyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
