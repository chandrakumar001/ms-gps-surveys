package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.error.exception.FieldValidationException;
import com.chandranedu.api.error.exception.ResourceNotFoundException;
import com.chandranedu.api.survey.entity.Survey;
import com.chandranedu.api.survey.entity.SurveyResponses;
import com.chandranedu.api.survey.entity.SurveyStatusEnum;
import com.chandranedu.api.survey.mapper.SurveyResponsesMapper;
import com.chandranedu.api.survey.repository.SurveyCommandRepository;
import com.chandranedu.api.survey.repository.SurveyResponsesRepository;
import com.chandranedu.api.survey.util.QuestionAnswerValidator;
import com.chandranedu.api.survey.validation.SurveyResponsesValidator;
import com.chandranedu.api.swagger.model.surveyresponses.SurveyResponsesRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.ERROR_SURVEY_ID_IS_NOT_FOUND;
import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT;
import static com.chandranedu.api.survey.mockdata.SurveyMockData.getSurveyResponses;
import static com.chandranedu.api.survey.mockdata.SurveyMockData.getSurveyWithQuestion;
import static com.chandranedu.api.survey.mockdata.SurveyResponsesMockData.getSurveyResponsesRequestDTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultSurveyResponseServiceTest {

    @InjectMocks
    private DefaultSurveyResponsesCommandService defaultSurveyResponsesService;
    @Mock
    private SurveyResponsesRepository surveyResponsesRepository;
    @Mock
    private SurveyCommandRepository surveyCommandRepository;
    @Mock
    private SurveyResponsesValidator surveyResponsesValidator;
    @Mock
    private QuestionAnswerValidator questionAnswerValidator;
    @Spy
    SurveyResponsesMapper surveyResponsesMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        defaultSurveyResponsesService = new DefaultSurveyResponsesCommandService(
                surveyResponsesRepository,
                surveyCommandRepository,
                surveyResponsesValidator,
                questionAnswerValidator,
                surveyResponsesMapper
        );
    }

    @Test
    void testDefaultSurveyResponsesCommandService_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new DefaultSurveyResponsesCommandService(null, null, null, null, null);
        });
        // then
        assertTrue(exception.getMessage().contains("surveyResponsesRepository is marked non-null but is null"));
    }

    @Test
    void testCreateSurveyResponses_if_survey_id_not_found_then_thrown() {
        // given
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();
        final SurveyResponsesRequestDTO surveyResponsesRequestDTO = new SurveyResponsesRequestDTO();

        when(surveyCommandRepository.findBySurveyStatus(Mockito.any(), any())).thenReturn(Optional.empty());
        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            defaultSurveyResponsesService.createSurveyResponses(reqQuestionId, surveyResponsesRequestDTO);
        });
        // then
        // Verifying  interactions
        verify(surveyCommandRepository, atLeast(0)).findBySurveyStatus(any(), any());
        verify(surveyResponsesRepository, atLeast(0)).save(any());
        assertTrue(exception.getMessage().contains(ERROR_SURVEY_ID_IS_NOT_FOUND));
    }

    @Test
    void testCreateSurveyResponses_success() {
        // given
        final UUID surveyId = UUID.randomUUID();
        final String reqSurveyId = surveyId.toString();
        final UUID questionId = UUID.randomUUID();
        final UUID answerId = UUID.randomUUID();
        final String surveyTitle = "sample survey title";
        final String active = "ACTIVE";
        final SurveyStatusEnum surveyStatusEnum = SurveyStatusEnum.valueOf(active);

        final SurveyResponsesRequestDTO surveyResponsesRequestDTO = getSurveyResponsesRequestDTO(
                questionId.toString(),
                answerId.toString()
        );
        final Survey survey = getSurveyWithQuestion(surveyId, surveyTitle, surveyStatusEnum);
        final SurveyResponses surveyResponses = getSurveyResponses();
        when(surveyCommandRepository.findBySurveyStatus(any(), any())).thenReturn(Optional.of(survey));
        when(surveyResponsesRepository.save(any())).thenReturn(surveyResponses);

        // when
        defaultSurveyResponsesService.createSurveyResponses(reqSurveyId, surveyResponsesRequestDTO);
        // then
        //Verifying  interactions
        verify(surveyCommandRepository, atLeast(0)).findBySurveyStatus(any(), any());
        verify(surveyResponsesRepository, atLeast(0)).save(any());
    }


    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    @DisplayName("SurveyById: Invalid uuid format")
    void testCreateSurveyResponses_failed(String reqQurveyId) {
        // when
        final SurveyResponsesRequestDTO surveyResponsesRequestDTO = new SurveyResponsesRequestDTO();

        Exception exception = assertThrows(FieldValidationException.class, () -> {
            defaultSurveyResponsesService.createSurveyResponses(reqQurveyId, surveyResponsesRequestDTO);
        });
        // then
        //Verifying  interactions
        verify(surveyCommandRepository, atLeast(0)).findBySurveyStatus(any(), any());
        verify(surveyResponsesRepository, atLeast(0)).save(any());
        assertTrue(exception.getMessage().contains(ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT));
    }
}
