package com.chandranedu.api.survey.mapper;

import com.chandranedu.api.survey.entity.Answer;
import com.chandranedu.api.survey.entity.Question;
import com.chandranedu.api.swagger.model.question.AnswerRequestDTO;
import com.chandranedu.api.swagger.model.question.AnswerResponseDTO;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class AnswerMapper {

    private AnswerMapper() {
        throw new IllegalStateException("AnswerMapper class");
    }

    public static Set<Answer> getAnswers(final List<AnswerRequestDTO> answersRequestDTO,
                                         final Question question) {

        final Set<AnswerRequestDTO> nonDuplicateAnswersRequestDTO = new HashSet<>(
                answersRequestDTO
        );
        if (CollectionUtils.isEmpty(nonDuplicateAnswersRequestDTO)) {
            return Collections.emptySet();
        }
        return nonDuplicateAnswersRequestDTO
                .stream()
                .filter(Objects::nonNull)
                .map(answerRequestDTO -> mapToAnswer(question, answerRequestDTO))
                .collect(Collectors.toSet());
    }

    public static Answer mapToAnswer(final Question question,
                                     final AnswerRequestDTO answerRequestDTO) {

        final Answer answer = new Answer();
        answer.setAnswerId(UUID.randomUUID());
        answer.setAnswerText(answerRequestDTO.getAnswer());
        answer.setIsCorrectAnswer(answerRequestDTO.getMarkCorrectAnswer());
        answer.setQuestion(question);
        return answer;
    }

    public static List<AnswerResponseDTO> getAnswerResponseDTO(final Set<Answer> answers) {

        if (answers.isEmpty()) {
            return Collections.emptyList();
        }
        return answers
                .stream()
                .map(AnswerMapper::mapToAnswerRequestDTO)
                .collect(Collectors.toList());
    }

    public static AnswerResponseDTO mapToAnswerRequestDTO(final Answer answer) {

        final AnswerResponseDTO answerRequestDTO = new AnswerResponseDTO();
        answerRequestDTO.setAnswerId(answer.getAnswerId());
        answerRequestDTO.setAnswerText(answer.getAnswerText());
        answerRequestDTO.isCorrectAnswer(answer.getIsCorrectAnswer());
        return answerRequestDTO;
    }
}
