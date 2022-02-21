package com.chandranedu.api.survey.controller.command;

import com.chandranedu.api.survey.service.QuestionCommandService;
import com.chandranedu.api.swagger.model.question.QuestionDTO;
import com.chandranedu.api.swagger.model.question.QuestionRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.chandranedu.api.survey.constant.QuestionConstant.PATH_PARAM_QUESTION_ID;


@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Question API")
public class QuestionCommandController {

    @NonNull QuestionCommandService questionCommandService;

    @PostMapping(value = "/v1/questions")
    public ResponseEntity<QuestionDTO> createQuestion(
            @RequestBody @Valid final QuestionRequestDTO questionRequestDTO) {

        final QuestionDTO questionDTO = questionCommandService.createQuestion(
                questionRequestDTO
        );
        return new ResponseEntity<>(questionDTO, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/v1/questions/{question-id}")
    public ResponseEntity<Void> deleteQuestionById(
            @PathVariable(value = PATH_PARAM_QUESTION_ID) final String reqQuestionId) {

        questionCommandService.deleteQuestionById(reqQuestionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
