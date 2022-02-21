package com.chandranedu.api.survey.controller.query;

import com.chandranedu.api.survey.service.SurveyResponsesQueryService;
import com.chandranedu.api.swagger.model.surveyresponses.ResponsesQuestionsResponseDTO;
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

import static com.chandranedu.api.survey.constant.SurveyConstant.PATH_PARAM_RESPONSE_ID;
import static com.chandranedu.api.survey.constant.SurveyConstant.PATH_PARAM_SURVEY_ID;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Survey Response API")
public class SurveyResponseQueryController {

    @NonNull SurveyResponsesQueryService surveyResponsesQueryService;

    @GetMapping(value = "/v1/surveys/{survey-id}/responses/{response-id}/summary")
    public ResponseEntity<ResponsesQuestionsResponseDTO> getSurveySummaryById(
            @PathVariable(value = PATH_PARAM_SURVEY_ID) final String reqSurveyId,
            @PathVariable(value = PATH_PARAM_RESPONSE_ID) final String reqResponsesId) {

        final ResponsesQuestionsResponseDTO questionsResponseDTO = surveyResponsesQueryService.getSurveySummaryById(
                reqSurveyId,
                reqResponsesId
        );
        return new ResponseEntity<>(questionsResponseDTO, HttpStatus.OK);
    }
}
