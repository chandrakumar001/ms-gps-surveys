package com.chandranedu.api.survey.mapper;

import com.chandranedu.api.survey.entity.*;
import com.chandranedu.api.swagger.model.surveyresponses.ResponsesAnswerRequestDTO;
import com.chandranedu.api.swagger.model.surveyresponses.ResponsesQuestionRequestDTO;
import com.chandranedu.api.swagger.model.surveyresponses.SurveyResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.chandranedu.api.survey.constant.SurveyResponseConstant.MSG_THE_SURVEY_HAS_BEEN_COMPLETED;
import static com.chandranedu.api.survey.entity.ResponsesAnswerStatusEnum.getResponsesAnswerStatusEnum;
import static com.chandranedu.api.survey.mapper.AuditMetadataMapper.mapToCreateAudit;
import static com.chandranedu.api.survey.util.QuestionAnswerUtils.*;

@Component
public class SurveyResponsesMapper {

    public SurveyResponseDTO mapToSurveyResponseDTO(SurveyResponses surveyResponses) {

        final SurveyResponseDTO surveyResponseDTO = new SurveyResponseDTO();
        surveyResponseDTO.setMessage(MSG_THE_SURVEY_HAS_BEEN_COMPLETED);
        surveyResponseDTO.setSurveyResponseId(surveyResponses.getSurveyResponseId());
        return surveyResponseDTO;
    }

    public SurveyResponses mapToSurveyResponses(
            final List<ResponsesQuestionRequestDTO> questions,
            final Survey survey,
            final Map<UUID, Question> questionMap) {

        final SurveyResponses surveyResponses = new SurveyResponses();
        final AuditMetadata auditMetadata = mapToCreateAudit();
        final Set<SurveyResponsesQuestion> surveyResponsesQuestions = getSurveyResponsesQuestions(
                questionMap,
                surveyResponses,
                questions
        );

        surveyResponses.setSurveyResponsesQuestions(surveyResponsesQuestions);
        surveyResponses.setSurveyResponseId(UUID.randomUUID());
        surveyResponses.setSurvey(survey);
        surveyResponses.setAuditMetadata(auditMetadata);
        return surveyResponses;
    }

    private Set<SurveyResponsesQuestion> getSurveyResponsesQuestions(
            final Map<UUID, Question> questionMap,
            final SurveyResponses surveyResponses,
            final List<ResponsesQuestionRequestDTO> questions) {
        return questions
                .stream()
                .map(mapToSurveyResponsesQuestionFunction(questionMap, surveyResponses))
                .collect(Collectors.toSet());
    }

    private Function<ResponsesQuestionRequestDTO, SurveyResponsesQuestion> mapToSurveyResponsesQuestionFunction(
            final Map<UUID, Question> questionMap,
            final SurveyResponses surveyResponses) {
        return questionRequestDTO -> mapToSurveyResponsesQuestion(questionMap, surveyResponses, questionRequestDTO);
    }

    public SurveyResponsesQuestion mapToSurveyResponsesQuestion(
            final Map<UUID, Question> questionMap,
            final SurveyResponses surveyResponses,
            final ResponsesQuestionRequestDTO questionRequestDTO) {

        final Question question = getQuestion(questionMap, questionRequestDTO.getQuestionId());
        final Map<UUID, Answer> answerMap = getAnswersMap(question);
        final SurveyResponsesQuestion surveyResponsesQuestion = new SurveyResponsesQuestion();
        final Set<SurveyResponsesAnswer> surveyResponsesAnswers = mapToSurveyResponsesAnswer(
                answerMap,
                questionRequestDTO.getAnswers(),
                surveyResponsesQuestion
        );
        surveyResponsesQuestion.setSurveyResponsesAnswers(surveyResponsesAnswers);
        surveyResponsesQuestion.setSurveyResponses(surveyResponses);
        surveyResponsesQuestion.setResponsesQuestionId(UUID.randomUUID());
        surveyResponsesQuestion.setQuestion(question);
        return surveyResponsesQuestion;
    }

    private Set<SurveyResponsesAnswer> mapToSurveyResponsesAnswer(final Map<UUID, Answer> answerMap,
                                                                  final List<ResponsesAnswerRequestDTO> answersRequestDTO,
                                                                  final SurveyResponsesQuestion surveyResponsesQuestion) {

        final Set<ResponsesAnswerRequestDTO> nonDuplicateAnswersRequestDTO = new HashSet<>(
                answersRequestDTO
        );
        if (CollectionUtils.isEmpty(nonDuplicateAnswersRequestDTO) || CollectionUtils.isEmpty(answerMap)) {
            return Collections.emptySet();
        }
        return nonDuplicateAnswersRequestDTO.stream()
                .filter(Objects::nonNull)
                .map(mapToSurveyResponsesAnswerFunction(answerMap, surveyResponsesQuestion))
                .collect(Collectors.toSet());
    }

    private Function<ResponsesAnswerRequestDTO, SurveyResponsesAnswer> mapToSurveyResponsesAnswerFunction(
            final Map<UUID, Answer> answerMap,
            final SurveyResponsesQuestion surveyResponsesQuestion) {
        return answerRequestDTO -> mapToSurveyResponsesAnswer(answerMap, surveyResponsesQuestion, answerRequestDTO);
    }

    private SurveyResponsesAnswer mapToSurveyResponsesAnswer(
            final Map<UUID, Answer> answerMap,
            final SurveyResponsesQuestion surveyResponsesQuestion,
            final ResponsesAnswerRequestDTO answerRequestDTO) {

        final Answer answer = getAnswer(answerMap, answerRequestDTO.getAnswerId());
        return mapToSurveyResponsesAnswer(surveyResponsesQuestion, answerRequestDTO, answer);
    }


    private SurveyResponsesAnswer mapToSurveyResponsesAnswer(
            final SurveyResponsesQuestion surveyResponsesQuestion,
            final ResponsesAnswerRequestDTO answerRequestDTO,
            final Answer answer) {

        final Boolean isSelectedAnswer = answerRequestDTO.getIsSelectedAnswer();
        final ResponsesAnswerStatusEnum statusEnum = getResponsesAnswerStatusEnum(
                isSelectedAnswer,
                answer.getIsCorrectAnswer()
        );

        final SurveyResponsesAnswer surveyResponsesAnswer = new SurveyResponsesAnswer();
        surveyResponsesAnswer.setResponsesAnswerId(UUID.randomUUID());
        surveyResponsesAnswer.setAnswer(answer);
        surveyResponsesAnswer.setSurveyResponsesQuestion(surveyResponsesQuestion);
        surveyResponsesAnswer.setIsSelectedAnswer(isSelectedAnswer);
        surveyResponsesAnswer.setStatus(statusEnum);
        return surveyResponsesAnswer;
    }
}
