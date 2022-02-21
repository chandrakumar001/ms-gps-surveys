package com.chandranedu.api.survey.mapper;


import com.chandranedu.api.survey.entity.AuditMetadata;
import com.chandranedu.api.survey.entity.Survey;
import com.chandranedu.api.survey.entity.SurveyStatusEnum;
import com.chandranedu.api.swagger.model.survey.SurveyDTO;
import com.chandranedu.api.swagger.model.survey.SurveyRequestDTO;
import com.chandranedu.api.swagger.model.survey.SurveyResponseDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.chandranedu.api.survey.entity.SurveyStatusEnum.getSurveyStatus;
import static com.chandranedu.api.survey.mapper.AuditMetadataMapper.mapToCreateAudit;

@Component
public class SurveyMapper {

    public SurveyDTO mapToSurveyDTO(final Survey survey) {

        final AuditMetadata auditMetadata = survey.getAuditMetadata();
        final SurveyResponseDTO data = mapToSurveysResponseDTO(survey);

        final SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setData(data);
        surveyDTO.setSurveyId(survey.getSurveyId());
        surveyDTO.setCreationDate(auditMetadata.getCreationDate().toString());
        surveyDTO.setUpdatedDate(auditMetadata.getUpdatedDate().toString());
        return surveyDTO;
    }

    private SurveyResponseDTO mapToSurveysResponseDTO(final Survey survey) {

        final SurveyResponseDTO data = new SurveyResponseDTO();
        data.setDescription(survey.getDescription());
        data.setSurveyTitle(survey.getSurveyTitle());
        data.setStatus(survey.getStatus().toString());
        return data;
    }

    public Survey mapToCreateSurvey(final SurveyRequestDTO surveyRequestDTO) {

        final SurveyStatusEnum status = getSurveyStatus(surveyRequestDTO.getStatus());
        final AuditMetadata auditMetadata = mapToCreateAudit();

        final Survey survey = new Survey();
        survey.setSurveyId(UUID.randomUUID());
        survey.setSurveyTitle(surveyRequestDTO.getTitle());
        survey.setDescription(surveyRequestDTO.getDescription());
        survey.setStatus(status);
        survey.setAuditMetadata(auditMetadata);
        return survey;
    }
}
