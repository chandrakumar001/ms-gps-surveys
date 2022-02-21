package com.chandranedu.api.survey.mapper;

import com.chandranedu.api.survey.entity.Question;
import com.chandranedu.api.survey.entity.Survey;
import com.chandranedu.api.survey.entity.SurveyQuestionAssign;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SurveyQuestionMapperAssign {

    public Set<SurveyQuestionAssign> mapToSurveyQuestion(final Survey existingSurvey,
                                                         final List<Question> existingQuestions) {

        if (CollectionUtils.isEmpty(existingQuestions)) {
            return Collections.emptySet();
        }
        return existingQuestions.stream()
                .map(getSurveyQuestionFunction(existingSurvey))
                .collect(Collectors.toSet());
    }

    public Function<Question, SurveyQuestionAssign> getSurveyQuestionFunction(
            final Survey existingSurvey) {
        return question -> mapToSurveyQuestion(existingSurvey, question);
    }


    private SurveyQuestionAssign mapToSurveyQuestion(final Survey existingSurvey,
                                                     final Question question) {

        final SurveyQuestionAssign surveyQuestionAssign = new SurveyQuestionAssign();
        surveyQuestionAssign.setSurveyQuestionAssignId(UUID.randomUUID());
        surveyQuestionAssign.setSurvey(existingSurvey);
        surveyQuestionAssign.setQuestion(question);
        return surveyQuestionAssign;
    }
}
