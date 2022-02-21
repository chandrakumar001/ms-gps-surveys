package com.chandranedu.api.survey.controller.command;

import com.chandranedu.api.survey.service.SurveyCommandService;
import com.chandranedu.api.swagger.model.survey.SurveyDTO;
import com.chandranedu.api.swagger.model.survey.SurveyRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.chandranedu.api.survey.constant.SurveyConstant.PATH_PARAM_SURVEY_ID;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Survey API")
public class SurveyCommandController {

    @NonNull SurveyCommandService surveyCommandService;

    @PostMapping(value = "/v1/surveys")
    public ResponseEntity<SurveyDTO> createSurvey(
            @RequestBody @Valid final SurveyRequestDTO surveyRequestDTO) {

        final SurveyDTO surveyDTO = surveyCommandService.createSurvey(
                surveyRequestDTO
        );
        return new ResponseEntity<>(surveyDTO, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/v1/surveys/{survey-id}")
    public ResponseEntity<Void> deleteSurveyById(
            @PathVariable(value = PATH_PARAM_SURVEY_ID) final String reqSurveyId) {

        surveyCommandService.deleteSurveyById(reqSurveyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
