package com.chandranedu.api.survey.entity.view;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
public class AnswerQueryView implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID answerId;
    private UUID questionId;

    private String answerText;
    private Boolean isCorrectAnswer;
}
