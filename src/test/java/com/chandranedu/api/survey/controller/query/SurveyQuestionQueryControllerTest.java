package com.chandranedu.api.survey.controller.query;

import com.chandranedu.api.survey.service.SurveyQuestionQueryService;
import com.chandranedu.api.swagger.model.survey.SurveyQuestionsResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.chandranedu.api.survey.ObjectMapperUtil.asJsonString;
import static com.chandranedu.api.survey.mockdata.SurveyMockData.getSurveyQuestionsResponseDTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SurveyQuestionQueryController.class)
class SurveyQuestionQueryControllerTest {

    @MockBean
    private SurveyQuestionQueryService surveyQuestionQueryService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSurveyQuestionQueryController_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new SurveyQuestionQueryController(null);
        });
        // then
        assertTrue(exception.getMessage().contains("surveyQuestionQueryService is marked non-null but is null"));
    }

    @Test
    void testGetSurveyById_success() throws Exception {
        //given
        final String surveyTitle = "what is survey?";
        final String active = "ACTIVE";
        final UUID surveyId = UUID.randomUUID();

        final SurveyQuestionsResponseDTO surveyQuestionsResponseDTO = getSurveyQuestionsResponseDTO(
                surveyTitle,
                active,
                surveyId
        );

        when(surveyQuestionQueryService.getSurveyById(any())).thenReturn(surveyQuestionsResponseDTO);
        //expected
        this.mockMvc.perform(get("/v1/surveys/" + surveyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(surveyQuestionsResponseDTO)));

    }

    @Test
    void testGetSurveyResponsesById_success() throws Exception {
        //given
        final String surveyTitle = "what is survey?";
        final String active = "ACTIVE";
        final UUID surveyId = UUID.randomUUID();
        final SurveyQuestionsResponseDTO surveyQuestionsResponseDTO = getSurveyQuestionsResponseDTO(
                surveyTitle,
                active,
                surveyId
        );

        when(surveyQuestionQueryService.getSurveyResponsesById(any())).thenReturn(surveyQuestionsResponseDTO);
        //expected
        this.mockMvc.perform(get("/v1/surveys/" + surveyId + "/responses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(surveyQuestionsResponseDTO)));
    }
}
