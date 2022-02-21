package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.error.exception.FieldValidationException;
import com.chandranedu.api.error.exception.ResourceNotFoundException;
import com.chandranedu.api.survey.entity.view.SurveyQuestionQueryView;
import com.chandranedu.api.survey.mapper.view.SurveyQuestionQueryViewMapper;
import com.chandranedu.api.survey.repository.SurveyQuestionQueryViewRepository;
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
import static com.chandranedu.api.survey.mockdata.SurveyMockData.getSurveyQuestionQueryView;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultSurveyQuestionQueryServiceTest {

    @InjectMocks
    private DefaultSurveyQuestionQueryService defaultSurveyQuestionQueryService;
    @Mock
    private SurveyQuestionQueryViewRepository queryViewRepository;
    @Spy
    private SurveyQuestionQueryViewMapper surveyMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        defaultSurveyQuestionQueryService = new DefaultSurveyQuestionQueryService(
                queryViewRepository,
                surveyMapper
        );
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    void testDefaultSurveyQuestionQueryService_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new DefaultSurveyQuestionQueryService(null, null);
        });
        // then
        assertTrue(exception.getMessage().contains("surveyQuestionQueryViewRepository is marked non-null but is null"));
    }

    @Test
    void testGetSurveyById_if_survey_id_not_found_then_thrown() {
        // given
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();

        when(queryViewRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            defaultSurveyQuestionQueryService.getSurveyById(reqQuestionId);
        });
        // then
        // Verifying  interactions
        verify(queryViewRepository, atLeast(0)).findById(any());
        verify(queryViewRepository, atLeast(0)).deleteById(any());
        assertTrue(exception.getMessage().contains(ERROR_SURVEY_ID_IS_NOT_FOUND));
    }

    @Test
    void testGetSurveyById_success() {
        // given
        final UUID surveyId = UUID.randomUUID();
        final String reqSurveyId = surveyId.toString();

        SurveyQuestionQueryView surveyQuestionQueryView = getSurveyQuestionQueryView(surveyId);
        when(queryViewRepository.findById(Mockito.any())).thenReturn(Optional.of(surveyQuestionQueryView));
        // when
        defaultSurveyQuestionQueryService.getSurveyById(reqSurveyId);
        // then
        //Verifying  interactions
        verify(queryViewRepository, atLeast(0)).findById(any());
        verify(queryViewRepository, atLeast(0)).deleteById(any());
    }


    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    @DisplayName("SurveyById: Invalid uuid format")
    void testGetSurveyById_failed(String reqQurveyId) {
        // when
        Exception exception = assertThrows(FieldValidationException.class, () -> {
            defaultSurveyQuestionQueryService.getSurveyById(reqQurveyId);
        });
        // then
        //Verifying  interactions
        verify(queryViewRepository, atLeast(0)).findById(any());
        verify(queryViewRepository, atLeast(0)).deleteById(any());
        assertTrue(exception.getMessage().contains(ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT));
    }


    @Test
    void testGetSurveyResponsesById_if_survey_id_not_found_then_thrown() {
        // given
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();

        when(queryViewRepository.findBySurveyStatus(any(), any())).thenReturn(Optional.empty());
        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            defaultSurveyQuestionQueryService.getSurveyResponsesById(reqQuestionId);
        });
        // then
        // Verifying  interactions
        verify(queryViewRepository, atLeast(0)).findBySurveyStatus(any(), any());
        verify(queryViewRepository, atLeast(0)).deleteById(any());
        assertTrue(exception.getMessage().contains(ERROR_SURVEY_ID_IS_NOT_FOUND));
    }

    @Test
    void testGetSurveyResponsesById_success() {
        // given
        final UUID surveyId = UUID.randomUUID();
        final String reqSurveyId = surveyId.toString();
        final SurveyQuestionQueryView surveyQuestionQueryView = getSurveyQuestionQueryView(surveyId);

        when(queryViewRepository.findBySurveyStatus(Mockito.any(), any()))
                .thenReturn(Optional.of(surveyQuestionQueryView));
        // when
        defaultSurveyQuestionQueryService.getSurveyResponsesById(reqSurveyId);
        // then
        //Verifying  interactions
        verify(queryViewRepository, atLeast(0)).findBySurveyStatus(any(), any());
        verify(queryViewRepository, atLeast(0)).deleteById(any());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    @DisplayName("SurveyById: Invalid uuid format")
    void testGetSurveyResponsesById_failed(String reqQurveyId) {
        // when
        Exception exception = assertThrows(FieldValidationException.class, () -> {
            defaultSurveyQuestionQueryService.getSurveyById(reqQurveyId);
        });
        // then
        //Verifying  interactions
        verify(queryViewRepository, atLeast(0)).findBySurveyStatus(any(), any());
        verify(queryViewRepository, atLeast(0)).deleteById(any());
        assertTrue(exception.getMessage().contains(ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT));
    }
}
