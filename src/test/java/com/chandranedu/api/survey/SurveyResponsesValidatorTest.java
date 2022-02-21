package com.chandranedu.api.survey;

import com.chandranedu.api.survey.validation.SurveyResponsesValidator;
import com.chandranedu.api.swagger.model.surveyresponses.ResponsesAnswerRequestDTO;
import com.chandranedu.api.swagger.model.surveyresponses.ResponsesQuestionRequestDTO;
import com.chandranedu.api.swagger.model.surveyresponses.SurveyResponsesRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.chandranedu.api.survey.mockdata.SurveyResponsesMockData.getResponsesQuestionRequestDTOS;
import static com.chandranedu.api.survey.mockdata.SurveyResponsesMockData.getSurveyResponsesRequestDTO;
import static com.chandranedu.api.survey.validation.SurveyResponsesValidator.INVALID_ANSWER_ID;
import static com.chandranedu.api.survey.validation.SurveyResponsesValidator.INVALID_QUESTION_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SurveyResponsesValidatorTest {

    SurveyResponsesValidator surveyResponsesValidator;

    @BeforeEach
    void setup() {
        surveyResponsesValidator = new SurveyResponsesValidator();
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    @DisplayName("Invalid Answer uuid format")
    void InvalidAnswerId(String answerId) {

        //given
        final String questionId = UUID.randomUUID().toString();
        final SurveyResponsesRequestDTO questionRequestDTO = getSurveyResponsesRequestDTO(
                questionId,
                answerId
        );
        //when
        final Optional<String> errorMsg = surveyResponsesValidator.validateSurveyResponsesRequestDTO(
                questionRequestDTO.getQuestions()
        );

        //then
        final String answerErrorMsg = errorMsg.get();
        assertTrue(answerErrorMsg.contains(INVALID_ANSWER_ID));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"e2a40b33", "e2a40b33-a11a", "e2a40b33-a11a-4938-8111", "e2a40b33-a11a-4938-8111-07acf5758978888"}
    )
    @DisplayName("Invalid Question uuid format")
    void InvalidQuestionId(String questionId) {

        //given
        final List<ResponsesQuestionRequestDTO> questions = List.of();
        //when
        final Optional<String> errorMsg = surveyResponsesValidator.validateSurveyResponsesRequestDTO(
                questions
        );

        //then
        final String answerErrorMsg = errorMsg.get();
        assertTrue(answerErrorMsg.contains(INVALID_QUESTION_ID));
    }

    @Test
    void InvalidAnswerIsEmpty() {

        //given
        final String questionId = UUID.randomUUID().toString();
        final List<ResponsesAnswerRequestDTO> answers = List.of();
        final List<ResponsesQuestionRequestDTO> questions = getResponsesQuestionRequestDTOS(
                questionId, answers
        );
        //when
        final Optional<String> errorMsg = surveyResponsesValidator.validateSurveyResponsesRequestDTO(
                questions
        );

        //then
        final String answerErrorMsg = errorMsg.get();
        assertTrue(answerErrorMsg.contains(INVALID_ANSWER_ID));
    }

    @Test
    void InvalidQuestionIsEmpty() {

        //given
        final List<ResponsesQuestionRequestDTO> questions = List.of();
        //when
        final Optional<String> errorMsg = surveyResponsesValidator.validateSurveyResponsesRequestDTO(
                questions
        );
        //then
        final String answerErrorMsg = errorMsg.get();
        assertTrue(answerErrorMsg.contains(INVALID_QUESTION_ID));
    }

    @Test
    void valid_questionId_and_answerId() {

        //given
        final String answerId = UUID.randomUUID().toString();
        final String questionId = UUID.randomUUID().toString();
        final SurveyResponsesRequestDTO questionRequestDTO = getSurveyResponsesRequestDTO(
                questionId,
                answerId
        );
        //when
        final Optional<String> errorMsg = surveyResponsesValidator.validateSurveyResponsesRequestDTO(
                questionRequestDTO.getQuestions()
        );

        //then
        assertTrue(errorMsg.isEmpty());
    }
}
