package com.chandranedu.api.survey.controller.query;

import com.chandranedu.api.survey.service.SurveyQuestionQueryService;
import com.chandranedu.api.swagger.model.survey.SurveyQuestionsResponseDTO;
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

import static com.chandranedu.api.survey.constant.SurveyConstant.PATH_PARAM_SURVEY_ID;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Survey API")
public class SurveyQuestionQueryController {

    @NonNull SurveyQuestionQueryService surveyQuestionQueryService;

    @GetMapping(value = "/v1/surveys/{survey-id}")
    public ResponseEntity<SurveyQuestionsResponseDTO> getSurveyById(
            @PathVariable(value = PATH_PARAM_SURVEY_ID) final String reqSurveyId) {

        final SurveyQuestionsResponseDTO surveyQuestionsResponseDTO = surveyQuestionQueryService.getSurveyById(
                reqSurveyId
        );
        return new ResponseEntity<>(surveyQuestionsResponseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/v1/surveys/{survey-id}/responses")
    public ResponseEntity<SurveyQuestionsResponseDTO> getSurveyResponsesById(
            @PathVariable(value = PATH_PARAM_SURVEY_ID) final String reqSurveyId) {

        final SurveyQuestionsResponseDTO surveyQuestionsResponseDTO = surveyQuestionQueryService.getSurveyResponsesById(
                reqSurveyId
        );
        return new ResponseEntity<>(surveyQuestionsResponseDTO, HttpStatus.OK);
    }
}
