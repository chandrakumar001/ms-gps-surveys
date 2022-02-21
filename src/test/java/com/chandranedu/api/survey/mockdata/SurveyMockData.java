package com.chandranedu.api.survey.mockdata;

import com.chandranedu.api.survey.entity.*;
import com.chandranedu.api.survey.entity.view.SurveyQuestionQueryView;
import com.chandranedu.api.swagger.model.survey.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SurveyMockData {

    public static Survey getSurveyWithQuestion(UUID surveyId,
                                               String surveyTitle,
                                               SurveyStatusEnum surveyStatusEnum) {

        Question question = QuestionMockData.getQuestion(UUID.randomUUID(), "some question", "some answer", true);
        SurveyQuestionAssign questionAssign = new SurveyQuestionAssign();
        questionAssign.setQuestion(question);
        questionAssign.setSurveyQuestionAssignId(UUID.randomUUID());

        Set<SurveyQuestionAssign> questionAssigns = Set.of(questionAssign);

        final Survey survey = getSurvey(surveyTitle, surveyStatusEnum, surveyId);
        survey.setSurveyQuestionAssigns(questionAssigns);
        return survey;
    }

    public static SurveyQuestionsResponseDTO getSurveyQuestionsResponseDTO(
            final String surveyTitle,
            final String active,
            final UUID surveyId) {

        final List<SurveyQuestionDTO> questions = List.of();
        final SurveyDTO surveyDTO = getSurveyDTO(surveyTitle, active, surveyId);
        final SurveyQuestionsResponseDTO surveyQuestionsResponseDTO = new SurveyQuestionsResponseDTO();
        surveyQuestionsResponseDTO.setQuestionCount(questions.size());
        surveyQuestionsResponseDTO.setSurvey(surveyDTO);
        surveyQuestionsResponseDTO.setQuestions(questions);
        return surveyQuestionsResponseDTO;
    }

    public static SurveyResponses getSurveyResponses() {
        final SurveyResponses surveyResponses = new SurveyResponses();
        surveyResponses.setSurveyResponseId(UUID.randomUUID());
        return surveyResponses;
    }

    public static SurveyQuestionQueryView getSurveyQuestionQueryView(UUID surveyId) {

        final AuditMetadata auditMetadata = MockAuditMetaData.getAuditMetadata();
        SurveyQuestionQueryView surveyQuestionQueryView = new SurveyQuestionQueryView();
        surveyQuestionQueryView.setSurveyId(surveyId);
        surveyQuestionQueryView.setStatus(SurveyStatusEnum.ACTIVE);
        surveyQuestionQueryView.setAuditMetadata(auditMetadata);
        return surveyQuestionQueryView;
    }

    public static Survey getSurvey(String surveyTitle,
                                   SurveyStatusEnum surveyStatusEnum,
                                   UUID surveyId) {

        final AuditMetadata auditMetadata = MockAuditMetaData.getAuditMetadata();
        final Survey survey = new Survey();
        survey.setDescription("some description");
        survey.setStatus(surveyStatusEnum);
        survey.setSurveyId(surveyId);
        survey.setSurveyTitle(surveyTitle);
        survey.setAuditMetadata(auditMetadata);
        return survey;
    }

    public static SurveyRequestDTO getSurveyRequestDTO(
            final String surveyTitle,
            final String active) {

        final SurveyRequestDTO surveyRequestDTO = new SurveyRequestDTO();
        surveyRequestDTO.setTitle(surveyTitle);
        surveyRequestDTO.setStatus(active);
        surveyRequestDTO.setDescription("Some description");
        return surveyRequestDTO;
    }

    public static SurveyDTO getSurveyDTO(final String surveyTitle,
                                         final String active,
                                         final UUID surveyId) {
        SurveyResponseDTO data = getSurveyResponseDTO(surveyTitle, active);
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setSurveyId(surveyId);
        surveyDTO.setData(data);
        surveyDTO.setUpdatedDate(LocalDateTime.now().toString());
        surveyDTO.setCreationDate(LocalDateTime.now().toString());
        return surveyDTO;
    }

    public static SurveyResponseDTO getSurveyResponseDTO(final String surveyTitle,
                                                         final String active) {


        SurveyResponseDTO surveyResponseDTO = new SurveyResponseDTO();
        surveyResponseDTO.setSurveyTitle(surveyTitle);
        surveyResponseDTO.setDescription("Some description");
        surveyResponseDTO.setStatus(active);
        return surveyResponseDTO;
    }
}
