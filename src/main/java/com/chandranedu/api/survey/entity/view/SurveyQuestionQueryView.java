package com.chandranedu.api.survey.entity.view;

import com.chandranedu.api.survey.entity.AuditMetadata;
import com.chandranedu.api.survey.entity.SurveyStatusEnum;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(schema = "survey", name = "survey_question_query_view")
@Setter
@Getter
@Immutable
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class SurveyQuestionQueryView implements Serializable {
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

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "question_answers_data")
    private Set<QuestionQueryView> questionAnswers;
}
