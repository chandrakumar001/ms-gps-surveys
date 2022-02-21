package com.chandranedu.api.survey.service.impl;

import com.chandranedu.api.error.exception.FieldValidationException;
import com.chandranedu.api.error.exception.NoRecordFoundException;
import com.chandranedu.api.error.exception.ResourceNotFoundException;
import com.chandranedu.api.survey.entity.view.QuestionQueryView;
import com.chandranedu.api.survey.mapper.view.QuestionQueryViewMapper;
import com.chandranedu.api.survey.repository.QuestionQueryViewRepository;
import com.chandranedu.api.swagger.model.question.QuestionsResponseDTO;
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

import static com.chandranedu.api.survey.constant.CommonConstant.NO_RECORD_FOUND;
import static com.chandranedu.api.survey.constant.QuestionErrorMessageConstant.ERROR_QUESTION_ID_IS_NOT_FOUND;
import static com.chandranedu.api.survey.constant.QuestionErrorMessageConstant.ERROR_THE_QUESTION_ID_IS_INVALID_UUID_FORMAT;
import static com.chandranedu.api.survey.mockdata.QuestionViewMockData.getQuestionQueryView;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultQuestionQueryServiceTest {

    @InjectMocks
    private DefaultQuestionQueryService defaultQuestionQueryService;
    @Mock
    private QuestionQueryViewRepository questionQueryViewRepository;
    @Spy
    private QuestionQueryViewMapper questionQueryViewMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        defaultQuestionQueryService = new DefaultQuestionQueryService(
                questionQueryViewRepository,
                questionQueryViewMapper
        );
    }

    @Test
    void testDefaultQuestionQueryService_constructor() {
        // when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new DefaultQuestionQueryService(null, questionQueryViewMapper);
        });
        // then
        assertTrue(exception.getMessage().contains("questionQueryViewRepository is marked non-null but is null"));
    }

    @Test
    void testQuestionAll_success() {
        // given
        final String questionTitle = "sample question title?";
        final String answerText = "sample answer text";
        final Boolean isCorrectAnswer = true;
        final UUID questionId = UUID.randomUUID();

        final QuestionQueryView questionQueryView = getQuestionQueryView(questionId, questionTitle, answerText, isCorrectAnswer);
        final List<QuestionQueryView> questionsQueryView = List.of(questionQueryView);
        when(questionQueryViewRepository.findAll()).thenReturn(questionsQueryView);
        // when
        QuestionsResponseDTO questions = defaultQuestionQueryService.getQuestions();
        // then
        //Verifying  interactions
        verify(questionQueryViewRepository, atLeast(1)).findAll();
        assertEquals(questionsQueryView.size(), questions.getCount());

    }

    @Test
    void testQuestionAll_if_questions_empty_no_record_found_then_thrown() {
        // given
        when(questionQueryViewRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        Exception exception = assertThrows(NoRecordFoundException.class, () -> {
            defaultQuestionQueryService.getQuestions();
        });
        // then
        // Verifying  interactions
        verify(questionQueryViewRepository, atLeast(1)).findAll();
        assertTrue(exception.getMessage().contains(NO_RECORD_FOUND));
    }

    @Test
    void testQuestionById_if_question_id_not_found_then_thrown() {
        // given
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();

        when(questionQueryViewRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            defaultQuestionQueryService.getQuestionById(reqQuestionId);
        });
        // then
        // Verifying  interactions
        verify(questionQueryViewRepository, atLeast(1)).findById(any());
        assertTrue(exception.getMessage().contains(ERROR_QUESTION_ID_IS_NOT_FOUND));
    }

    @Test
    void testQuestionById_success() {
        // given
        final String questionTitle = "sample question title?";
        final String answerText = "sample answer text";
        final Boolean isCorrectAnswer = true;
        final UUID questionId = UUID.randomUUID();
        final String reqQuestionId = questionId.toString();

        final QuestionQueryView question = getQuestionQueryView(questionId, questionTitle, answerText, isCorrectAnswer);
        when(questionQueryViewRepository.findById(Mockito.any())).thenReturn(Optional.of(question));
        // when
        defaultQuestionQueryService.getQuestionById(reqQuestionId);
        // then
        //Verifying  interactions
        verify(questionQueryViewRepository, atLeast(1)).findById(any());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    @DisplayName("QuestionById: Valid uuid format")
    void testQuestionById_failed(String reqQuestionId) {
        // when
        Exception exception = assertThrows(FieldValidationException.class, () -> {
            defaultQuestionQueryService.getQuestionById(reqQuestionId);
        });
        // then
        //Verifying  interactions
        verify(questionQueryViewRepository, atLeast(0)).findById(any());
        assertTrue(exception.getMessage().contains(ERROR_THE_QUESTION_ID_IS_INVALID_UUID_FORMAT));
    }
}
