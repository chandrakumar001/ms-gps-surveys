package com.chandranedu.api.survey.controller.command;

import com.chandranedu.api.survey.service.SurveyQuestionAssignCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.chandranedu.api.survey.constant.SurveyConstant.PATH_PARAM_SURVEY_ID;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Survey Question Assignment API")
public class SurveyQuestionAssignCommandController {

    @NonNull SurveyQuestionAssignCommandService assignService;

    @PostMapping(value = "/v1/surveys/{survey-id}/questions-assignment")
    public ResponseEntity<Void> assignSurveyQuestionById(
            @PathVariable(value = PATH_PARAM_SURVEY_ID) final String reqSurveyId) {

        assignService.assignSurveyQuestionById(reqSurveyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
