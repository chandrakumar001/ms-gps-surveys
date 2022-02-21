package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.error.exception.FieldValidationException;
import com.chandranedu.api.error.exception.ResourceAlreadyExistsException;
import com.chandranedu.api.error.exception.ResourceNotFoundException;
import com.chandranedu.api.survey.entity.Survey;
import com.chandranedu.api.survey.entity.SurveyQuestionAssign;
import com.chandranedu.api.survey.mapper.SurveyQuestionMapperAssign;
import com.chandranedu.api.survey.repository.QuestionCommandRepository;
import com.chandranedu.api.survey.repository.SurveyCommandRepository;
import com.chandranedu.api.survey.repository.SurveyQuestionAssignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.chandranedu.api.survey.constant.SurveyErrorMessageConstant.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultSurveyQuestionAssignCommandServiceTest {

    @InjectMocks
    private DefaultSurveyQuestionAssignCommandService defaultSurveyQuestionAssignService;
    @Mock
    private QuestionCommandRepository questionCommandRepository;
    @Mock
    private SurveyCommandRepository surveyCommandRepository;
    @Mock
    private SurveyQuestionAssignRepository surveyQuestionAssignRepository;
    @Spy
    private SurveyQuestionMapperAssign assignMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        defaultSurveyQuestionAssignService = new DefaultSurveyQuestionAssignCommandService(
                questionCommandRepository,
                surveyCommandRepository,
                surveyQuestionAssignRepository,
                assignMapper
        );
    }

    @Test
    void testDefaultSurveyQuestionAssignCommandService_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new DefaultSurveyQuestionAssignCommandService(null, null, null, null);
        });
        // then
        assertTrue(exception.getMessage().contains("questionCommandRepository is marked non-null but is null"));
    }

    @Test
    void testAssignSurveyQuestionById_if_question_id_not_found_then_thrown() {
        // given
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();

        when(surveyQuestionAssignRepository.findBySurveySurveyId(Mockito.any())).thenReturn(Collections.emptyList());
        when(surveyCommandRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            defaultSurveyQuestionAssignService.assignSurveyQuestionById(reqQuestionId);
        });
        // then
        // Verifying  interactions
        verify(surveyQuestionAssignRepository, atLeast(1)).findBySurveySurveyId(any());
        verify(surveyCommandRepository, atLeast(1)).findById(any());
        verify(questionCommandRepository, atLeast(0)).findAll();
        assertTrue(exception.getMessage().contains(ERROR_SURVEY_ID_IS_NOT_FOUND));
    }

    @Test
    void testAssignSurveyQuestionById_if_question_already_assigned_then_thrown() {
        // given
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();

        final SurveyQuestionAssign questionAssign = new SurveyQuestionAssign();
        when(surveyQuestionAssignRepository.findBySurveySurveyId(Mockito.any()))
                .thenReturn(List.of(questionAssign));
        // when
        Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            defaultSurveyQuestionAssignService.assignSurveyQuestionById(reqQuestionId);
        });
        // then
        // Verifying  interactions
        verify(surveyQuestionAssignRepository, atLeast(1)).findBySurveySurveyId(any());
        verify(surveyCommandRepository, atLeast(0)).findById(any());
        verify(questionCommandRepository, atLeast(0)).findAll();
        assertTrue(exception.getMessage().contains(SURVEY_ID_IS_ALREADY_ASSIGNED));
    }

    @Test
    void testAssignSurveyQuestionById_success() {
        // given
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();
        final Survey survey = new Survey();
        when(surveyCommandRepository.findById(Mockito.any())).thenReturn(Optional.of(survey));
        // when
        defaultSurveyQuestionAssignService.assignSurveyQuestionById(reqQuestionId);
        // then
        //Verifying  interactions
        verify(surveyQuestionAssignRepository, atLeast(1)).findBySurveySurveyId(any());
        verify(surveyCommandRepository, atLeast(1)).findById(any());
        verify(questionCommandRepository, atLeast(1)).findAll();
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    @DisplayName("QuestionById: Valid uuid format")
    void testAssignSurveyQuestionById_failed(String reqQuestionId) {
        // when
        Exception exception = assertThrows(FieldValidationException.class, () -> {
            defaultSurveyQuestionAssignService.assignSurveyQuestionById(reqQuestionId);
        });
        // then
        //Verifying  interactions
        verify(surveyQuestionAssignRepository, atLeast(0)).findBySurveySurveyId(any());
        verify(questionCommandRepository, atLeast(0)).findAll();
        verify(surveyCommandRepository, atLeast(0)).findById(any());
        assertTrue(exception.getMessage().contains(ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT));
    }
}
