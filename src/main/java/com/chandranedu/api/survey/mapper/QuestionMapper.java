package com.chandranedu.api.survey.mapper;


import com.chandranedu.api.survey.entity.Answer;
import com.chandranedu.api.survey.entity.AuditMetadata;
import com.chandranedu.api.survey.entity.Question;
import com.chandranedu.api.swagger.model.question.AnswerResponseDTO;
import com.chandranedu.api.swagger.model.question.QuestionDTO;
import com.chandranedu.api.swagger.model.question.QuestionRequestDTO;
import com.chandranedu.api.swagger.model.question.QuestionResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.chandranedu.api.survey.mapper.AnswerMapper.getAnswerResponseDTO;
import static com.chandranedu.api.survey.mapper.AnswerMapper.getAnswers;
import static com.chandranedu.api.survey.mapper.AuditMetadataMapper.mapToCreateAudit;

@Component
public class QuestionMapper {

    public Question mapToCreateQuestion(final QuestionRequestDTO questionRequestDTO) {

        final AuditMetadata auditMetadata = mapToCreateAudit();
        final UUID questionId = UUID.randomUUID();

        final Question question = new Question();
        final Set<Answer> answers = getAnswers(
                questionRequestDTO.getAnswers(),
                question
        );

        question.setQuestionId(questionId);
        question.setQuestionTitle(questionRequestDTO.getQuestionTitle());
        question.setAnswers(answers);
        question.setAuditMetadata(auditMetadata);
        return question;
    }

    public QuestionDTO mapToQuestionDTO(final Question question) {

        final AuditMetadata auditMetadata = question.getAuditMetadata();
        final QuestionResponseDTO data = mapToQuestionResponseDTO(question);

        final QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setData(data);
        questionDTO.setQuestionId(question.getQuestionId());
        questionDTO.setCreationDate(auditMetadata.getCreationDate().toString());
        questionDTO.setUpdatedDate(auditMetadata.getUpdatedDate().toString());
        return questionDTO;
    }

    public QuestionResponseDTO mapToQuestionResponseDTO(final Question question) {

        final List<AnswerResponseDTO> answersRequestDTO = getAnswerResponseDTO(
                question.getAnswers()
        );

        final QuestionResponseDTO data = new QuestionResponseDTO();
        data.setQuestionTitle(question.getQuestionTitle());
        data.setAnswers(answersRequestDTO);
        return data;
    }
}
