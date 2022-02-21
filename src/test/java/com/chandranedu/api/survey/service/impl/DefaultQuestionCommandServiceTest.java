package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.error.exception.FieldValidationException;
import com.chandranedu.api.error.exception.ResourceAlreadyExistsException;
import com.chandranedu.api.error.exception.ResourceNotFoundException;
import com.chandranedu.api.survey.entity.Question;
import com.chandranedu.api.survey.entity.SurveyQuestionAssign;
import com.chandranedu.api.survey.mapper.QuestionMapper;
import com.chandranedu.api.survey.repository.QuestionCommandRepository;
import com.chandranedu.api.survey.repository.SurveyQuestionAssignRepository;
import com.chandranedu.api.swagger.model.question.QuestionDTO;
import com.chandranedu.api.swagger.model.question.QuestionRequestDTO;
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

import static com.chandranedu.api.survey.constant.QuestionErrorMessageConstant.*;
import static com.chandranedu.api.survey.mockdata.QuestionMockData.getQuestion;
import static com.chandranedu.api.survey.mockdata.QuestionMockData.getQuestionResponseDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultQuestionCommandServiceTest {

    @InjectMocks
    private DefaultQuestionCommandService questionCommandService;
    @Mock
    private QuestionCommandRepository questionCommandRepository;
    @Mock
    private SurveyQuestionAssignRepository surveyQuestionRepository;
    @Spy
    private QuestionMapper questionMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        questionCommandService = new DefaultQuestionCommandService(
                questionCommandRepository,
                surveyQuestionRepository,
                questionMapper
        );
    }

    @Test
    void testDefaultQuestionCommandService_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new DefaultQuestionCommandService(null, null, null);
        });
        // then
        assertTrue(exception.getMessage().contains("questionCommandRepository is marked non-null but is null"));
    }

    @Test
    void testCreateQuestion_success() {
        // given
        final String questionTitle = "sample question title?";
        final String answerText = "sample answer text";
        final Boolean isCorrectAnswer = true;
        final UUID questionId = UUID.randomUUID();

        final QuestionRequestDTO questionRequestDTO = getQuestionResponseDTO(
                questionTitle,
                answerText,
                isCorrectAnswer
        );
        final Question question = getQuestion(questionId, questionTitle, answerText, isCorrectAnswer);
        when(questionCommandRepository.save(any())).thenReturn(question);
        // when
        final QuestionDTO questionDTO = questionCommandService.createQuestion(
                questionRequestDTO
        );
        // then
        assertEquals(1, questionDTO.getData().getAnswers().size());
        verify(questionCommandRepository, atLeast(1)).save(any());
    }

    @Test
    void testDeleteQuestionById_if_question_already_linked_to_survey_then_thrown() {
        // given
        final String questionTitle = "sample question title?";
        final String answerText = "sample answer text";
        final Boolean isCorrectAnswer = true;
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();

        final Question question = getQuestion(questionId, questionTitle, answerText, isCorrectAnswer);
        when(questionCommandRepository.findById(Mockito.any())).thenReturn(Optional.of(question));
        final SurveyQuestionAssign questionAssign = new SurveyQuestionAssign();
        when(surveyQuestionRepository.findByQuestionQuestionId(any())).thenReturn(List.of(questionAssign));
        // when
        final Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            questionCommandService.deleteQuestionById(reqQuestionId);
        });
        // then
        // Verifying  interactions
        verify(questionCommandRepository, atLeast(1)).findById(any());
        verify(surveyQuestionRepository, atLeast(1)).findByQuestionQuestionId(any());
        assertTrue(exception.getMessage().contains(ERROR_ALREADY_THE_QUESTION_ID_IS_LINKED_WITH_SURVEY));

    }

    @Test
    void testDeleteQuestionById_if_question_id_not_found_then_thrown() {
        // given
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();

        when(questionCommandRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            questionCommandService.deleteQuestionById(reqQuestionId);
        });
        // then
        // Verifying  interactions
        verify(questionCommandRepository, atLeast(1)).findById(any());
        verify(surveyQuestionRepository, atLeast(0)).findByQuestionQuestionId(any());

        assertTrue(exception.getMessage().contains(ERROR_QUESTION_ID_IS_NOT_FOUND));
    }

    @Test
    void testDeleteQuestionById_success() {
        // given
        final String questionTitle = "sample question title?";
        final String answerText = "sample answer text";
        final Boolean isCorrectAnswer = true;
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();

        final Question question = getQuestion(questionId, questionTitle, answerText, isCorrectAnswer);
        when(questionCommandRepository.findById(Mockito.any())).thenReturn(Optional.of(question));
        when(surveyQuestionRepository.findByQuestionQuestionId(any())).thenReturn(Collections.emptyList());
        // when
        questionCommandService.deleteQuestionById(reqQuestionId);
        // then
        //Verifying  interactions
        verify(questionCommandRepository, atLeast(1)).findById(any());
        verify(surveyQuestionRepository, atLeast(1)).findByQuestionQuestionId(any());
        verify(questionCommandRepository, atLeast(1)).deleteById(any());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    @DisplayName("QuestionById: Valid uuid format")
    void testDeleteQuestionById_failed(String reqQuestionId) {
        // when
        Exception exception = assertThrows(FieldValidationException.class, () -> {
            questionCommandService.deleteQuestionById(reqQuestionId);
        });
        // then
        //Verifying  interactions
        verify(questionCommandRepository, atLeast(0)).findById(any());
        verify(surveyQuestionRepository, atLeast(0)).findByQuestionQuestionId(any());
        verify(questionCommandRepository, atLeast(0)).deleteById(any());
        assertTrue(exception.getMessage().contains(ERROR_THE_QUESTION_ID_IS_INVALID_UUID_FORMAT));
    }
}
