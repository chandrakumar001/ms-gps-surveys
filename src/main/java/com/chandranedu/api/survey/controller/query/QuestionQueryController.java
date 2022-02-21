package com.chandranedu.api.survey.controller.query;

import com.chandranedu.api.survey.service.QuestionQueryService;
import com.chandranedu.api.swagger.model.question.QuestionDTO;
import com.chandranedu.api.swagger.model.question.QuestionsResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.chandranedu.api.survey.constant.QuestionConstant.PATH_PARAM_QUESTION_ID;


@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Question API")
public class QuestionQueryController {

    @NonNull QuestionQueryService questionQueryService;

    @GetMapping(value = "/v1/questions")
    public ResponseEntity<QuestionsResponseDTO> getQuestions() {

        final QuestionsResponseDTO questionResponseDTO = questionQueryService.getQuestions();
        return new ResponseEntity<>(questionResponseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/v1/questions/{question-id}")
    public ResponseEntity<QuestionDTO> getQuestionById(
            @PathVariable(value = PATH_PARAM_QUESTION_ID) final String reqQuestionId) {

        final QuestionDTO questionDTO = questionQueryService.getQuestionById(
                reqQuestionId
        );
        return new ResponseEntity<>(questionDTO, HttpStatus.OK);
    }
}
