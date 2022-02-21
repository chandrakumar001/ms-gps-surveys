package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.error.exception.FieldValidationException;
import com.chandranedu.api.error.exception.ResourceNotFoundException;
import com.chandranedu.api.survey.entity.Survey;
import com.chandranedu.api.survey.entity.SurveyStatusEnum;
import com.chandranedu.api.survey.mapper.SurveyMapper;
import com.chandranedu.api.survey.repository.SurveyCommandRepository;
import com.chandranedu.api.swagger.model.survey.SurveyDTO;
import com.chandranedu.api.swagger.model.survey.SurveyRequestDTO;
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
import static com.chandranedu.api.survey.entity.SurveyStatusEnum.INVALID_STATUS;
import static com.chandranedu.api.survey.mockdata.SurveyMockData.getSurveyRequestDTO;
import static com.chandranedu.api.survey.mockdata.SurveyMockData.getSurveyWithQuestion;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultSurveyCommandServiceTest {

    @InjectMocks
    private DefaultSurveyCommandService defaultSurveyCommandService;
    @Mock
    private SurveyCommandRepository surveyCommandRepository;
    @Spy
    private SurveyMapper surveyMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        defaultSurveyCommandService = new DefaultSurveyCommandService(
                surveyCommandRepository,
                surveyMapper
        );
    }

    @Test
    void testDefaultSurveyCommandService_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new DefaultSurveyCommandService(null, null);
        });
        // then
        assertTrue(exception.getMessage().contains("surveyCommandRepository is marked non-null but is null"));
    }

    @Test
    void testCreateSurvey_success() {
        // given
        final UUID surveyId = UUID.randomUUID();
        final String surveyTitle = "sample survey title";
        final String active = "ACTIVE";
        final SurveyStatusEnum surveyStatusEnum = SurveyStatusEnum.valueOf(active);
        final SurveyRequestDTO surveyRequestDTO = getSurveyRequestDTO(
                surveyTitle,
                active
        );
        final Survey survey = getSurveyWithQuestion(surveyId, surveyTitle, surveyStatusEnum);

        when(surveyCommandRepository.save(any())).thenReturn(survey);
        // when
        final SurveyDTO questionDTO = defaultSurveyCommandService.createSurvey(
                surveyRequestDTO
        );
        // then
        assertEquals(questionDTO.getData().getSurveyTitle(), surveyTitle);
        verify(surveyCommandRepository, atLeast(1)).save(any());
    }


    @Test
    void testCreateSurvey_if_invalid_status_then_thrown() {
        // given
        final String surveyTitle = "what is survey?";
        final String active = "test";

        final SurveyRequestDTO surveyRequestDTO = getSurveyRequestDTO(
                surveyTitle,
                active
        );
        // when
        Exception exception = assertThrows(FieldValidationException.class,
                () -> defaultSurveyCommandService.createSurvey(surveyRequestDTO)
        );
        // then
        verify(surveyCommandRepository, atLeast(0)).save(any());
        assertTrue(exception.getMessage().contains(INVALID_STATUS));
    }

    @Test
    void testDeleteSurveyById_if_survey_id_not_found_then_thrown() {
        // given
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();

        when(surveyCommandRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            defaultSurveyCommandService.deleteSurveyById(reqQuestionId);
        });
        // then
        // Verifying  interactions
        verify(surveyCommandRepository, atLeast(0)).findById(any());
        verify(surveyCommandRepository, atLeast(0)).deleteById(any());
        assertTrue(exception.getMessage().contains(ERROR_SURVEY_ID_IS_NOT_FOUND));
    }

    @Test
    void testDeleteSurveyById_success() {
        // given
        final UUID surveyId = UUID.randomUUID();
        final String reqSurveyId = surveyId.toString();

        Survey survey = new Survey();
        when(surveyCommandRepository.findById(Mockito.any())).thenReturn(Optional.of(survey));
        // when
        defaultSurveyCommandService.deleteSurveyById(reqSurveyId);
        // then
        //Verifying  interactions
        verify(surveyCommandRepository, atLeast(0)).findById(any());
        verify(surveyCommandRepository, atLeast(0)).deleteById(any());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    @DisplayName("QuestionById: Valid uuid format")
    void testDeleteSurveyById_failed(String reqQurveyId) {
        // when
        Exception exception = assertThrows(FieldValidationException.class, () -> {
            defaultSurveyCommandService.deleteSurveyById(reqQurveyId);
        });
        // then
        //Verifying  interactions
        verify(surveyCommandRepository, atLeast(0)).findById(any());
        verify(surveyCommandRepository, atLeast(0)).deleteById(any());
        assertTrue(exception.getMessage().contains(ERROR_THE_SURVEY_ID_IS_INVALID_UUID_FORMAT));
    }
}
