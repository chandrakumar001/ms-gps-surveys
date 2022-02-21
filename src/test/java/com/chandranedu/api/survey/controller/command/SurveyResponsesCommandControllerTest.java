package com.chandranedu.api.survey.controller.command;

import com.chandranedu.api.survey.service.SurveyResponsesCommandService;
import com.chandranedu.api.swagger.model.surveyresponses.SurveyResponseDTO;
import com.chandranedu.api.swagger.model.surveyresponses.SurveyResponsesRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.chandranedu.api.survey.ObjectMapperUtil.asJsonString;
import static com.chandranedu.api.survey.mockdata.SurveyResponsesMockData.getSurveyResponseDTO;
import static com.chandranedu.api.survey.mockdata.SurveyResponsesMockData.getSurveyResponsesRequestDTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SurveyResponsesCommandController.class)
class SurveyResponsesCommandControllerTest {

    @MockBean
    private SurveyResponsesCommandService surveyResponsesCommandService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSurveyResponsesCommandController_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new SurveyResponsesCommandController(null);
        });
        // then
        assertTrue(exception.getMessage().contains("surveyResponsesCommandService is marked non-null but is null"));
    }


    @Test
    void testCreateSurveyResponses() throws Exception {
        //given
        final UUID questionId = UUID.randomUUID();
        final UUID responseId = UUID.randomUUID();
        final UUID answerId = UUID.randomUUID();
        final UUID reqSurveyId = UUID.randomUUID();

        final SurveyResponseDTO surveyResponseDTO = getSurveyResponseDTO(
                responseId
        );
        final SurveyResponsesRequestDTO surveyResponsesRequestDTO = getSurveyResponsesRequestDTO(
                questionId.toString(),
                answerId.toString()
        );
        when(surveyResponsesCommandService.createSurveyResponses(reqSurveyId.toString(), surveyResponsesRequestDTO))
                .thenReturn(surveyResponseDTO);

        //expected
        this.mockMvc.perform(post("/v1/surveys/" + reqSurveyId + "/responses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(surveyResponsesRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(asJsonString(surveyResponseDTO)));
    }
}
