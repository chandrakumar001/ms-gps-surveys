package com.chandranedu.api.survey.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "survey_responses_answer",schema = "responses")
@Setter
@Getter
public class SurveyResponsesAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID responsesAnswerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    private Boolean isSelectedAnswer;
    @Enumerated(EnumType.STRING)
    private ResponsesAnswerStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responses_question_id")
    private SurveyResponsesQuestion surveyResponsesQuestion;

}
