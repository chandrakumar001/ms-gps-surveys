package com.chandranedu.api.survey.controller.command;

import com.chandranedu.api.survey.service.SurveyResponsesCommandService;
import com.chandranedu.api.swagger.model.surveyresponses.SurveyResponseDTO;
import com.chandranedu.api.swagger.model.surveyresponses.SurveyResponsesRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.chandranedu.api.survey.constant.SurveyConstant.PATH_PARAM_SURVEY_ID;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Survey Response API")
public class SurveyResponsesCommandController {


    @NonNull SurveyResponsesCommandService surveyResponsesCommandService;

    @PostMapping(value = "/v1/surveys/{survey-id}/responses")
    public ResponseEntity<SurveyResponseDTO> createSurveyResponsesById(
            @PathVariable(value = PATH_PARAM_SURVEY_ID) final String reqSurveyId,
            @RequestBody @Valid final SurveyResponsesRequestDTO surveyResponsesRequestDTO) {

        final SurveyResponseDTO surveyResponseDTO = surveyResponsesCommandService.createSurveyResponses(
                reqSurveyId,
                surveyResponsesRequestDTO
        );
        return new ResponseEntity<>(surveyResponseDTO, HttpStatus.CREATED);
    }
}
