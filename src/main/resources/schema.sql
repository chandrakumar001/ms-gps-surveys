drop schema responses cascade;
drop schema questions cascade;
drop schema survey cascade;
create schema responses;
create schema questions;
create schema survey;
create table questions.answer (answer_id uuid not null, answer_text varchar(255), is_correct_answer boolean, question_id uuid);
create table questions.question (question_id uuid not null, creation_date timestamp, updated_date timestamp, question_title varchar(255));
create table responses.survey_responses (survey_response_id uuid not null, creation_date timestamp, updated_date timestamp, survey_id uuid);
create table responses.survey_responses_answer (responses_answer_id uuid not null, is_selected_answer boolean, status varchar(255), answer_id uuid, responses_question_id uuid);
create table responses.survey_responses_question (responses_question_id uuid not null, question_id uuid, survey_response_id uuid);
create table survey.survey (survey_id uuid not null, creation_date timestamp, updated_date timestamp, description varchar(255), status varchar(255), survey_title varchar(255));
create table survey.survey_question_assign (survey_question_assign_id uuid not null, question_id uuid, survey_id uuid);
--create table question_query_view (question_id uuid not null, answers jsonb, creation_date timestamp, updated_date timestamp, question_title varchar(255));
-----PK
alter table questions.answer add constraint answer_id_pkey primary key (answer_id);
alter table questions.question add constraint question_id_pkey primary key (question_id);
alter table responses.survey_responses add constraint survey_response_id_pkey primary key (survey_response_id);
alter table responses.survey_responses_answer add constraint responses_answer_id_pkey primary key (responses_answer_id);
alter table responses.survey_responses_question add constraint responses_question_id_pkey primary key (responses_question_id);
alter table survey.survey add constraint survey_id_pkey primary key (survey_id);
alter table survey.survey_question_assign add constraint survey_question_assign_id_pkey primary key (survey_question_assign_id);
--FK
alter table if exists questions.answer add constraint question_id_fk foreign key (question_id) references questions.question;
alter table if exists responses.survey_responses add constraint survey_id_fk foreign key (survey_id) references survey.survey;
alter table if exists responses.survey_responses_answer add constraint answer_id_fk foreign key (answer_id) references questions.answer;
alter table if exists responses.survey_responses_answer add constraint responses_question_id_fk foreign key (responses_question_id) references responses.survey_responses_question;
alter table if exists responses.survey_responses_question add constraint question_id_fk foreign key (question_id) references questions.question;
alter table if exists responses.survey_responses_question add constraint survey_response_id_fk foreign key (survey_response_id) references responses.survey_responses;
alter table if exists survey.survey_question_assign add constraint question_id_fk foreign key (question_id) references questions.question;
alter table if exists survey.survey_question_assign add constraint survey_id_fk foreign key (survey_id) references survey.survey;
------survey_responses_query_view--------------
create or replace view survey.survey_responses_query_view
as
select
    sra.*,
    s.status,
    (select coalesce (jsonb_agg( distinct (questions.*)),'[]'::jsonb ) as questions from
        (select srq.question_id as "questionId",q.question_title as "questionTitle",
            (select coalesce (jsonb_agg(answers.*),'[]'::jsonb ) as answers from
                (select a.answer_id as "answerId" ,sra.is_selected_answer as "isSelectedAnswer",sra.status ,a.is_correct_answer as "isCorrectAnswer" ,a.answer_text as "answerText"
                from responses.survey_responses_answer sra
                join questions.answer  a on sra.answer_id =a.answer_id ) answers) as answers
        from  responses.survey_responses_question srq join questions.question q on q.question_id=srq.question_id
        ) questions)
from responses.survey_responses sra
join survey.survey s on sra.survey_id=s.survey_id;
------survey_question_query_view--------------
create or replace view survey.survey_question_query_view
as
select
    s2.*,
    (select coalesce (jsonb_agg( distinct (question_answers_data.*)),'[]'::jsonb ) as question_answers_data from
    (select asq.question_id as "questionId",q.question_title as "questionTitle",
    (select coalesce (jsonb_agg(answers.*),'[]'::jsonb ) as answers from
    (select a.answer_id as "answerId" ,a.is_correct_answer as "isCorrectAnswer" ,a.answer_text as "answerText",q.question_id as "questionId"
    from questions.answer  a where q.question_id =a.question_id ) answers) as answers
    from  survey.survey_question_assign asq  join questions.question q on q.question_id=asq.question_id
    ) question_answers_data)
from survey.survey s2;
-----------question_query_view
create or replace view questions.question_query_view
as
select
    q.*,
    (select coalesce (jsonb_agg(distinct(answers.*)),'[]'::jsonb )as answers from
    (select a.answer_id as "answerId" ,a.is_correct_answer as "isCorrectAnswer" ,a.answer_text as "answerText" ,q.question_id as "questionId"
    from questions.answer a where q.question_id =a.question_id) answers) as answers
from questions.question q;