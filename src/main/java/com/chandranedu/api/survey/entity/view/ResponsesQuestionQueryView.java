package com.chandranedu.api.survey.entity.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class ResponsesQuestionQueryView implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID questionId;
    private String questionTitle;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Set<ResponsesAnswerQueryView> answers;
}
