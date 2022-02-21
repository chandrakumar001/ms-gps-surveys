package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.error.exception.FieldValidationException;
import com.chandranedu.api.error.exception.ResourceNotFoundException;
import com.chandranedu.api.survey.entity.view.SurveyResponsesQueryView;
import com.chandranedu.api.survey.mapper.SurveyResponseSummaryMapper;
import com.chandranedu.api.survey.repository.SurveyResponsesQueryViewRepository;
import com.chandranedu.api.survey.service.ResponsesSummaryService;
import com.chandranedu.api.swagger.model.surveyresponses.ResponsesQuestionsResponseDTO;
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

import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.*;
import static com.chandranedu.api.survey.mockdata.SurveyResponsesMockData.getSurveyResponsesQueryView;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultResponsesSummaryServiceTest {

    @InjectMocks
    private DefaultSurveyResponsesQueryService defaultSurveyResponsesQueryService;
    @Mock
    private SurveyResponsesQueryViewRepository queryViewRepository;
    @Spy
    private SurveyResponseSummaryMapper summaryMapper;
    @Spy
    private ResponsesSummaryService summaryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        defaultSurveyResponsesQueryService = new DefaultSurveyResponsesQueryService(
                queryViewRepository,
                summaryService,
                summaryMapper
        );
    }

    @Test
    void testDefaultSurveyResponsesQueryService_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new DefaultSurveyResponsesQueryService(null, null, null);
        });
        // then
        assertTrue(exception.getMessage().contains("summaryViewRepository is marked non-null but is null"));
    }

    @Test
    void testGetSurveyById_if_survey_id_and_responses_id_not_found_then_thrown() {
        // given
        final String reqResponsesId = UUID.randomUUID().toString();
        final String surveyById = UUID.randomUUID().toString();

        when(queryViewRepository.findBySurveyResponseIdAndSurveyId(Mockito.any(), any()))
                .thenReturn(Optional.empty());
        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            defaultSurveyResponsesQueryService.getSurveySummaryById(surveyById, reqResponsesId);
        });
        // then
        // Verifying  interactions
        verify(queryViewRepository, atLeast(0)).findBySurveyResponseIdAndSurveyId(any(), any());
        assertTrue(exception.getMessage().contains(ERROR_SURVEY_ID_AND_RESPONSE_ARE_NOT_FOUND));
    }

    @Test
    void testGetSurveyById_success() {
        // given
        final UUID reqResponsesId = UUID.randomUUID();
        final UUID surveyById = UUID.randomUUID();

        final SurveyResponsesQueryView surveyResponsesQueryView = getSurveyResponsesQueryView(
                reqResponsesId,
                surveyById
        );
        when(queryViewRepository.findBySurveyResponseIdAndSurveyId(Mockito.any(), any()))
                .thenReturn(Optional.of(surveyResponsesQueryView));
        // when
        final ResponsesQuestionsResponseDTO surveySummaryById = defaultSurveyResponsesQueryService.getSurveySummaryById(
                surveyById.toString(),
                reqResponsesId.toString()
        );
        // then
        // Verifying  interactions
        verify(queryViewRepository, atLeast(1)).findBySurveyResponseIdAndSurveyId(any(), any());
    }


    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    @DisplayName("SurveyById: Invalid uuid format")
    void testGetSurveyResponsesById_surveyById_failed(String surveyById) {
        final String reqResponsesId = UUID.randomUUID().toString();

        // when
        Exception exception = assertThrows(FieldValidationException.class, () -> {
            defaultSurveyResponsesQueryService.getSurveySummaryById(surveyById, reqResponsesId);
        });
        // then
        //Verifying  interactions
        verify(queryViewRepository, atLeast(0)).findBySurveyResponseIdAndSurveyId(any(), any());
        assertTrue(exception.getMessage().contains(ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    @DisplayName("reqResponsesId: Invalid uuid format")
    void testGetSurveyResponsesById_reqResponsesId_failed(String reqResponsesId) {
        final String surveyById = UUID.randomUUID().toString();

        // when
        Exception exception = assertThrows(FieldValidationException.class, () -> {
            defaultSurveyResponsesQueryService.getSurveySummaryById(surveyById, reqResponsesId);
        });
        // then
        //Verifying  interactions
        verify(queryViewRepository, atLeast(0)).findBySurveyResponseIdAndSurveyId(any(), any());
        assertTrue(exception.getMessage().contains(ERROR_THE_RESPONSE_ID_IS_INVALID_UUID_FORMAT));
    }
}
