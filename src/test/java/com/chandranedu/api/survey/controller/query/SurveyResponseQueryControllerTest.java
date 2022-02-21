package com.chandranedu.api.survey.controller.query;

import com.chandranedu.api.survey.service.SurveyResponsesQueryService;
import com.chandranedu.api.swagger.model.surveyresponses.ResponsesQuestionsResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.chandranedu.api.survey.ObjectMapperUtil.asJsonString;
import static com.chandranedu.api.survey.mockdata.SurveyResponsesMockData.getResponsesQuestionsResponseDTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SurveyResponseQueryController.class)
class SurveyResponseQueryControllerTest {

    @MockBean
    private SurveyResponsesQueryService surveyResponsesQueryService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSurveyResponseQueryController_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new SurveyResponseQueryController(null);
        });
        // then
        assertTrue(exception.getMessage().contains("surveyResponsesQueryService is marked non-null but is null"));
    }

    @Test
    void testGetSurveySummaryById_success() throws Exception {
        //given
        final UUID surveyId = UUID.randomUUID();
        final UUID responseId = UUID.randomUUID();

        final ResponsesQuestionsResponseDTO responsesQuestionsResponseDTO = getResponsesQuestionsResponseDTO(
        );

        when(surveyResponsesQueryService.getSurveySummaryById(any(), any()))
                .thenReturn(responsesQuestionsResponseDTO);
        //expected
        this.mockMvc.perform(get("/v1/surveys/" + surveyId + "/responses/" + responseId + "/summary")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(responsesQuestionsResponseDTO)));
    }
}
