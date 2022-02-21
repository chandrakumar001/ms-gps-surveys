package com.chandranedu.api.survey.mockdata;

import com.chandranedu.api.survey.entity.AuditMetadata;
import com.chandranedu.api.survey.entity.view.AnswerQueryView;
import com.chandranedu.api.survey.entity.view.QuestionQueryView;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class QuestionViewMockData {

    public static QuestionQueryView getQuestionQueryView(final UUID questionId,
                                                         final String questionTitle,
                                                         final String answerText,
                                                         final Boolean isCorrectAnswer) {

        final AnswerQueryView answer = getAnswerQueryView(answerText, isCorrectAnswer);
        final Set<AnswerQueryView> answersQueryView = Set.of(answer);
        final AuditMetadata auditMetadata = getAuditMetadata();

        final QuestionQueryView queryView = new QuestionQueryView();
        queryView.setAnswers(answersQueryView);
        queryView.setQuestionId(questionId);
        queryView.setQuestionTitle(questionTitle);
        queryView.setAuditMetadata(auditMetadata);
        return queryView;
    }

    private static AuditMetadata getAuditMetadata() {

        final AuditMetadata auditMetadata = new AuditMetadata();
        auditMetadata.setUpdatedDate(LocalDateTime.now());
        auditMetadata.setCreationDate(LocalDateTime.now());
        return auditMetadata;
    }

    private static AnswerQueryView getAnswerQueryView(final String answerText,
                                                      final Boolean isCorrectAnswer) {

        final AnswerQueryView answer = new AnswerQueryView();
        answer.setIsCorrectAnswer(isCorrectAnswer);
        answer.setAnswerId(UUID.randomUUID());
        answer.setAnswerText(answerText);
        return answer;
    }
}
