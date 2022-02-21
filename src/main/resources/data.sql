INSERT INTO questions.question
(question_id, creation_date, updated_date, question_title)
VALUES('5a420559-e201-4463-8f57-bcdbdc3f6370', '2022-02-21 15:46:46.784', '2022-02-21 15:46:46.784', 'Which of the following device do you have');

INSERT INTO questions.answer
(answer_id, answer_text, is_correct_answer, question_id) values
('b8289d59-9f2e-4422-b773-3a1385cb6851', 'Smart watch', true, '5a420559-e201-4463-8f57-bcdbdc3f6370')
,('7fcba4c0-232b-4c82-b640-40fc75e1ac52', 'Laptop', true, '5a420559-e201-4463-8f57-bcdbdc3f6370')
,('9e8488fd-08f0-4ff3-9824-97d736a8ee53', 'Smartphone', true, '5a420559-e201-4463-8f57-bcdbdc3f6370')
,('7901d2d8-9766-45cf-a1a5-c40a10510254', 'Smart TV', false, '5a420559-e201-4463-8f57-bcdbdc3f6370');


INSERT INTO survey.survey
(survey_id, creation_date, updated_date, description, status, survey_title)values
('ae3731f6-4679-43ad-bd3a-d481ba51e516', '2022-02-20 17:35:11.663', '2022-02-20 17:35:11.663', 'some description', 'ACTIVE', 'some titile');

INSERT INTO survey.survey_question_assign
(survey_question_assign_id, question_id, survey_id) values
('5b62ec93-1999-424f-975d-5e99768773c8', '5a420559-e201-4463-8f57-bcdbdc3f6370', 'ae3731f6-4679-43ad-bd3a-d481ba51e516');


INSERT INTO responses.survey_responses
(survey_response_id, creation_date, updated_date, survey_id) values
('7692560b-e05c-467a-aa16-48e8715fb5a1', '2022-02-21 16:05:05.100', '2022-02-21 16:05:05.100', 'ae3731f6-4679-43ad-bd3a-d481ba51e516');

INSERT INTO responses.survey_responses_answer
(responses_answer_id, is_selected_answer, status, answer_id, responses_question_id) values
('3c1462b1-86af-4c99-b802-e5eb7eee0675', true, 'CORRECT_ANSWER', 'b8289d59-9f2e-4422-b773-3a1385cb6851', '0ed5a4ef-ec98-4c76-9e9e-bc1589557c4e');

INSERT INTO responses.survey_responses_question
(responses_question_id, question_id, survey_response_id) values
('0ed5a4ef-ec98-4c76-9e9e-bc1589557c45', '5a420559-e201-4463-8f57-bcdbdc3f6370', '7692560b-e05c-467a-aa16-48e8715fb5a1');
