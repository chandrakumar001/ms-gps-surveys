package com.chandranedu.api.survey.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "survey_responses_question",schema = "responses")
@Setter
@Getter
public class SurveyResponsesQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID responsesQuestionId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "surveyResponsesQuestion"
    )
    private Set<SurveyResponsesAnswer> surveyResponsesAnswers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_response_id")
    private SurveyResponses surveyResponses;

}
