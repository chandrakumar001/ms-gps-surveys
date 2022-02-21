package com.chandranedu.api.survey.util;

import com.chandranedu.api.survey.entity.Answer;
import com.chandranedu.api.survey.entity.Question;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QuestionAnswerUtils {

    private QuestionAnswerUtils() {
        throw new IllegalStateException("QuestionAnswerUtils class");
    }

    public static Map<UUID, Answer> getAnswersMap(final Question question) {
        if (Objects.isNull(question)) {
            return Collections.emptyMap();
        }
        return getAnswersMap(question.getAnswers());
    }

    public static Map<UUID, Answer> getAnswersMap(final Set<Answer> answers) {
        if (CollectionUtils.isEmpty(answers)) {
            return Collections.emptyMap();
        }
        return answers.stream()
                .collect(Collectors.toMap(Answer::getAnswerId, Function.identity()));
    }

    public static Answer getAnswer(final Map<UUID, Answer> answerMap,
                                   final UUID questionId) {
        return answerMap.get(questionId);
    }

    public static Answer getAnswer(final Map<UUID, Answer> answerMap,
                                   final String questionId) {
        return getAnswer(answerMap, UUID.fromString(questionId));
    }

    public static Question getQuestion(final Map<UUID, Question> questionMap,
                                       final UUID questionId) {
        return questionMap.get(questionId);
    }

    public static Question getQuestion(final Map<UUID, Question> questionMap,
                                       final String questionId) {
        return getQuestion(questionMap, UUID.fromString(questionId));
    }

    public static boolean isQuestionId(final Map<UUID, Question> questionMap,
                                       final UUID questionId) {
        return questionMap.containsKey(questionId);
    }

    public static boolean isQuestionId(final Map<UUID, Question> questionMap,
                                       final String questionId) {
        return isQuestionId(questionMap, UUID.fromString(questionId));
    }

    public static boolean isAnswerId(final Map<UUID, Answer> answerMap,
                                     final UUID answerId) {
        return answerMap.containsKey(answerId);
    }

    public static boolean isAnswerId(final Map<UUID, Answer> answerMap,
                                     final String answerId) {
        return isAnswerId(answerMap, UUID.fromString(answerId));
    }

    public static boolean isAnswerFound(final Map<UUID, Question> questionMap,
                                        final UUID questionId) {

        final Question question = getQuestion(questionMap, questionId);
        final Map<UUID, Answer> answerMap = getAnswersMap(question);
        return !CollectionUtils.isEmpty(answerMap);
    }

    public static boolean isAnswerFound(final Map<UUID, Question> questionMap,
                                        final String questionId) {
        return isAnswerFound(questionMap, UUID.fromString(questionId));
    }
}