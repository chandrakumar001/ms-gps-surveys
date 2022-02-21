package com.chandranedu.api.survey.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "survey",schema = "survey")
@Setter
@Getter
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID surveyId;

    private String surveyTitle;
    private String description;
    @Enumerated(EnumType.STRING)
    private SurveyStatusEnum status;
    @Embedded
    private AuditMetadata auditMetadata;

    @OneToMany(
            mappedBy = "survey",
            cascade = CascadeType.ALL
    )
    private Set<SurveyQuestionAssign> surveyQuestionAssigns;

    @OneToMany(
            mappedBy = "survey",
            cascade = CascadeType.ALL
    )
    private Set<SurveyResponses> surveyResponses = new HashSet<>();
}
