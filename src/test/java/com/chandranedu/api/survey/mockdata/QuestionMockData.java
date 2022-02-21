package com.chandranedu.api.survey.mockdata;

import com.chandranedu.api.survey.entity.Answer;
import com.chandranedu.api.survey.entity.AuditMetadata;
import com.chandranedu.api.survey.entity.Question;
import com.chandranedu.api.swagger.model.question.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.chandranedu.api.survey.mockdata.MockAuditMetaData.getAuditMetadata;

public class QuestionMockData {

    public static QuestionsResponseDTO getQuestionsResponseDTO(String questionTitle,
                                                               String answerText,
                                                               Boolean isCorrectAnswer) {

        QuestionDTO questionDTO = getQuestionDTO(questionTitle, answerText, isCorrectAnswer);
        QuestionsResponseDTO questionsResponseDTO = new QuestionsResponseDTO();

        final List<QuestionDTO> questionsDTO = List.of(questionDTO);
        questionsResponseDTO.setQuestions(questionsDTO);
        questionsResponseDTO.setCount(questionsDTO.size());
        return questionsResponseDTO;
    }

    public static QuestionDTO getQuestionDTO(String questionTitle,
                                             String answerText,
                                             Boolean isCorrectAnswer) {

        AnswerResponseDTO answerResponseDTO = new AnswerResponseDTO();
        answerResponseDTO.setAnswerId(UUID.randomUUID());
        answerResponseDTO.setAnswerText(answerText);
        answerResponseDTO.setIsCorrectAnswer(isCorrectAnswer);

        List<AnswerResponseDTO> answers = List.of(answerResponseDTO);
        QuestionResponseDTO data = new QuestionResponseDTO();
        data.setAnswers(answers);

        data.setQuestionTitle(questionTitle);
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setData(data);
        questionDTO.setQuestionId(UUID.randomUUID());
        questionDTO.setCreationDate(LocalDateTime.now().toString());
        questionDTO.setUpdatedDate(LocalDateTime.now().toString());
        return questionDTO;
    }

    public static QuestionRequestDTO getQuestionResponseDTO(final String questionTitle,
                                                            final String answer,
                                                            final Boolean isCorrectAnswer) {
        final AnswerRequestDTO answerResponseDTO = getAnswerResponseDTO(
                answer,
                isCorrectAnswer
        );
        List<AnswerRequestDTO> answersDTO = List.of(answerResponseDTO);
        QuestionRequestDTO data = new QuestionRequestDTO();
        data.setQuestionTitle(questionTitle);
        data.setAnswers(answersDTO);
        return data;
    }

    public static AnswerRequestDTO getAnswerResponseDTO(final String answerText,
                                                        final Boolean isCorrectAnswer) {

        final AnswerRequestDTO answerResponseDTO = new AnswerRequestDTO();
        answerResponseDTO.setAnswer(answerText);
        answerResponseDTO.setMarkCorrectAnswer(isCorrectAnswer);
        return answerResponseDTO;
    }

    public static Question getQuestion(final UUID questionId,
                                       final String questionTitle,
                                       final String answerText,
                                       final Boolean isCorrectAnswer) {

        final Answer answer = getAnswer(answerText, isCorrectAnswer);
        final Set<Answer> answers = Set.of(answer);
        final AuditMetadata auditMetadata = getAuditMetadata();

        final Question question = new Question();
        question.setAnswers(answers);
        question.setQuestionId(questionId);
        question.setQuestionTitle(questionTitle);
        question.setAuditMetadata(auditMetadata);
        return question;
    }

    public static Answer getAnswer(final String answerText,
                                   final Boolean isCorrectAnswer) {

        final Answer answer = new Answer();
        answer.setIsCorrectAnswer(isCorrectAnswer);
        answer.setAnswerId(UUID.randomUUID());
        answer.setAnswerText(answerText);
        return answer;
    }
}
