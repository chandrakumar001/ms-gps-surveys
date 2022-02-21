package com.chandranedu.api.survey.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "survey_responses",schema = "responses")
@Setter
@Getter
public class SurveyResponses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID surveyResponseId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "surveyResponses"
    )
    private Set<SurveyResponsesQuestion> surveyResponsesQuestions = new HashSet<>();

    @Embedded
    private AuditMetadata auditMetadata;
}
