package com.chandranedu.api.survey.controller.command;

import com.chandranedu.api.survey.service.SurveyQuestionAssignCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SurveyQuestionAssignCommandController.class)
class SurveyQuestionAssignCommandControllerTest {

    @MockBean
    private SurveyQuestionAssignCommandService assignCommandService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSurveyQuestionAssignCommandController_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new SurveyQuestionAssignCommandController(null);
        });
        // then
        assertTrue(exception.getMessage().contains("assignService is marked non-null but is null"));
    }

    @Test
    void testAssignSurveyQuestionById() throws Exception {
        //given
        final String reqSurveyId = UUID.randomUUID().toString();

        doNothing().when(assignCommandService).assignSurveyQuestionById(reqSurveyId);
        //expected
        this.mockMvc.perform(post("/v1/surveys/" + reqSurveyId + "/questions-assignment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
