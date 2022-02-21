package com.chandranedu.api.survey.mockdata;

import com.chandranedu.api.survey.entity.AuditMetadata;
import com.chandranedu.api.survey.entity.ResponsesAnswerStatusEnum;
import com.chandranedu.api.survey.entity.SurveyStatusEnum;
import com.chandranedu.api.survey.entity.view.ResponsesAnswerQueryView;
import com.chandranedu.api.survey.entity.view.ResponsesQuestionQueryView;
import com.chandranedu.api.survey.entity.view.SurveyQuestionQueryView;
import com.chandranedu.api.survey.entity.view.SurveyResponsesQueryView;
import com.chandranedu.api.swagger.model.surveyresponses.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.chandranedu.api.survey.constant.SurveyResponseConstant.MSG_THE_SURVEY_HAS_BEEN_COMPLETED;

public class SurveyResponsesMockData {

    public static SurveyResponsesQueryView getSurveyResponsesQueryView(
            UUID reqResponsesId,
            UUID surveyById) {

        ResponsesAnswerQueryView answerQueryView = new ResponsesAnswerQueryView();
        answerQueryView.setAnswerId(UUID.randomUUID());
        answerQueryView.setAnswerText("some answer");
        answerQueryView.setStatus(ResponsesAnswerStatusEnum.CORRECT_ANSWER);
        answerQueryView.setIsCorrectAnswer(true);
        answerQueryView.setIsSelectedAnswer(true);
        Set<ResponsesAnswerQueryView> answers = Set.of(answerQueryView);
        ResponsesQuestionQueryView responsesQuestionQueryView = new ResponsesQuestionQueryView();
        responsesQuestionQueryView.setQuestionId(UUID.randomUUID());
        responsesQuestionQueryView.setQuestionTitle("some question title");
        responsesQuestionQueryView.setAnswers(answers);

        final Set<ResponsesQuestionQueryView> questions = Set.of(responsesQuestionQueryView);
        final AuditMetadata auditMetadata = MockAuditMetaData.getAuditMetadata();
        final SurveyResponsesQueryView surveyResponsesQueryView = new SurveyResponsesQueryView();
        surveyResponsesQueryView.setQuestions(questions);
        surveyResponsesQueryView.setSurveyId(surveyById);
        surveyResponsesQueryView.setSurveyResponseId(reqResponsesId);
        surveyResponsesQueryView.setAuditMetadata(auditMetadata);
        return surveyResponsesQueryView;
    }

    public static ResponsesQuestionsResponseDTO getResponsesQuestionsResponseDTO() {

        List<ResponsesQuestionDTO> questions = List.of();
        QuestionSummaryDTO summary = new QuestionSummaryDTO();
        summary.setTotalQuestion(0);
        summary.setTotalIncorrectAnswer(0);
        summary.setTotalCorrectAnswer(0);
        ResponsesQuestionsResponseDTO responsesQuestionsResponseDTO = new ResponsesQuestionsResponseDTO();
        responsesQuestionsResponseDTO.setQuestions(questions);
        responsesQuestionsResponseDTO.setSummary(summary);
        return responsesQuestionsResponseDTO;
    }

    public static SurveyResponsesRequestDTO getSurveyResponsesRequestDTO(
            final String questionId,
            final String answerId) {

        final ResponsesAnswerRequestDTO responsesAnswerRequestDTO = getResponsesAnswerRequestDTO(answerId);
        final List<ResponsesAnswerRequestDTO> answers = List.of(responsesAnswerRequestDTO);
        final List<ResponsesQuestionRequestDTO> questions = getResponsesQuestionRequestDTOS(
                questionId, answers
        );
        final SurveyResponsesRequestDTO surveyResponsesRequestDTO = new SurveyResponsesRequestDTO();
        surveyResponsesRequestDTO.setQuestions(questions);
        return surveyResponsesRequestDTO;
    }

    private static ResponsesAnswerRequestDTO getResponsesAnswerRequestDTO(String answerId) {
        final ResponsesAnswerRequestDTO responsesAnswerRequestDTO = new ResponsesAnswerRequestDTO();
        responsesAnswerRequestDTO.setIsSelectedAnswer(true);
        responsesAnswerRequestDTO.setAnswerId(answerId);
        return responsesAnswerRequestDTO;
    }

    public static List<ResponsesQuestionRequestDTO> getResponsesQuestionRequestDTOS(String questionId, List<ResponsesAnswerRequestDTO> answers) {
        final ResponsesQuestionRequestDTO questionRequestDTO = getResponsesQuestionRequestDTO(questionId, answers);
        return List.of(questionRequestDTO);
    }

    private static ResponsesQuestionRequestDTO getResponsesQuestionRequestDTO(String questionId, List<ResponsesAnswerRequestDTO> answers) {
        final ResponsesQuestionRequestDTO questionRequestDT = new ResponsesQuestionRequestDTO();
        questionRequestDT.setQuestionId(questionId);
        questionRequestDT.setAnswers(answers);
        return questionRequestDT;
    }

    private static SurveyQuestionQueryView getSurveyQuestionQueryView(UUID surveyId) {

        final AuditMetadata auditMetadata = MockAuditMetaData.getAuditMetadata();
        SurveyQuestionQueryView surveyQuestionQueryView = new SurveyQuestionQueryView();
        surveyQuestionQueryView.setSurveyId(surveyId);
        surveyQuestionQueryView.setStatus(SurveyStatusEnum.ACTIVE);
        surveyQuestionQueryView.setAuditMetadata(auditMetadata);
        return surveyQuestionQueryView;
    }

    public static SurveyResponseDTO getSurveyResponseDTO(UUID responseId) {

        SurveyResponseDTO surveyResponseDTO = new SurveyResponseDTO();
        surveyResponseDTO.setMessage(MSG_THE_SURVEY_HAS_BEEN_COMPLETED);
        surveyResponseDTO.setSurveyResponseId(responseId);
        return surveyResponseDTO;
    }
}
