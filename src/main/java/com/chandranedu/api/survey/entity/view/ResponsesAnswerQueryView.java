package com.chandranedu.api.survey.entity.view;

import com.chandranedu.api.survey.entity.ResponsesAnswerStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
public class ResponsesAnswerQueryView implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID answerId;
    private String answerText;
    private Boolean isCorrectAnswer;
    private Boolean isSelectedAnswer;
    @Enumerated(EnumType.STRING)
    private ResponsesAnswerStatusEnum status;
}
