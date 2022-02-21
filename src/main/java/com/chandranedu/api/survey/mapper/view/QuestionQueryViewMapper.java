package com.chandranedu.api.survey.mapper.view;

import com.chandranedu.api.survey.entity.AuditMetadata;
import com.chandranedu.api.survey.entity.view.AnswerQueryView;
import com.chandranedu.api.survey.entity.view.QuestionQueryView;
import com.chandranedu.api.swagger.model.question.AnswerResponseDTO;
import com.chandranedu.api.swagger.model.question.QuestionDTO;
import com.chandranedu.api.swagger.model.question.QuestionResponseDTO;
import com.chandranedu.api.swagger.model.question.QuestionsResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class QuestionQueryViewMapper {

    public QuestionsResponseDTO mapToQuestionResponseDTO(final List<QuestionQueryView> questionsView) {

        final List<QuestionDTO> questionsDTO = getSurveysDTO(questionsView);
        final Map<UUID, List<AnswerResponseDTO>> answerRequestDTOMap = convertToAnswersResponseDTO(
                questionsView
        );
        final List<QuestionDTO> questionAnswerUpdated = getQuestionDTO(questionsDTO, answerRequestDTOMap);

        final QuestionsResponseDTO questionsResponseDTO = new QuestionsResponseDTO();
        questionsResponseDTO.setQuestions(questionAnswerUpdated);
        questionsResponseDTO.setCount(questionAnswerUpdated.size());
        return questionsResponseDTO;
    }

    private List<QuestionDTO> getQuestionDTO(final List<QuestionDTO> questionsDTO,
                                             final Map<UUID, List<AnswerResponseDTO>> answerRequestDTOMap) {
        return questionsDTO.stream()
                .peek(setAnswerAndGetQuestionDTOConsumer(answerRequestDTOMap))
                .collect(Collectors.toList());
    }

    private Consumer<QuestionDTO> setAnswerAndGetQuestionDTOConsumer(
            final Map<UUID, List<AnswerResponseDTO>> answerRequestDTOMap) {

        return questionDTO -> {
            final List<AnswerResponseDTO> queryView = answerRequestDTOMap.get(questionDTO.getQuestionId());
            final QuestionResponseDTO data = questionDTO.getData();
            data.setAnswers(queryView);
        };
    }

    private Map<UUID, List<AnswerResponseDTO>> convertToAnswersResponseDTO(
            final List<QuestionQueryView> questionsView) {
        return questionsView.stream()
                .map(QuestionQueryView::getAnswers)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(QuestionQueryViewMapper::isQuestionIdNull)
                .collect(Collectors.groupingBy(
                        AnswerQueryView::getQuestionId,
                        Collectors.mapping(AnswerQueryViewMapper::mapToAnswerRequestDTO, Collectors.toList()))
                );
    }

    private static boolean isQuestionIdNull(AnswerQueryView answerQueryView) {
        return Objects.nonNull(answerQueryView.getQuestionId());
    }

    private List<QuestionDTO> getSurveysDTO(final List<QuestionQueryView> questionsView) {
        return questionsView.stream()
                .filter(Objects::nonNull)
                .map(this::mapToQuestionDTO)
                .collect(Collectors.toList());
    }

    public QuestionDTO mapToQuestionDTO(final QuestionQueryView questionQueryView) {

        final AuditMetadata auditMetadata = questionQueryView.getAuditMetadata();

        final List<AnswerResponseDTO> answersDTO = getAnswersResponseDTO(
                questionQueryView.getAnswers()
        );
        final QuestionResponseDTO data = mapToQuestionResponseDTO(questionQueryView);
        data.setAnswers(answersDTO);

        final QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setData(data);
        questionDTO.setQuestionId(questionQueryView.getQuestionId());
        questionDTO.setCreationDate(auditMetadata.getCreationDate().toString());
        questionDTO.setUpdatedDate(auditMetadata.getUpdatedDate().toString());
        return questionDTO;
    }

    private List<AnswerResponseDTO> getAnswersResponseDTO(final Set<AnswerQueryView> answers) {
        if (CollectionUtils.isEmpty(answers)) {
            return Collections.emptyList();
        }
        return answers.stream()
                .map(AnswerQueryViewMapper::mapToAnswerRequestDTO)
                .collect(Collectors.toList());
    }


    private QuestionResponseDTO mapToQuestionResponseDTO(final QuestionQueryView questionQueryView) {
        final QuestionResponseDTO data = new QuestionResponseDTO();
        data.setQuestionTitle(questionQueryView.getQuestionTitle());
        return data;
    }
}
