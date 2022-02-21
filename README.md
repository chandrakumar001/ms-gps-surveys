# ms-gps-surveys

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Application URL Swagger](#Application-URL-Swagger)
* [Cloudfoundry](#Cloudfoundry)
* [Kubernetes](#Kubernetes)

## General info
This project for ms-gps-surveys microservice and used lasted version spring boot, swagger(OpenAPI),Code Generator swagger.

#### Technologies

 * Java: 11
 * Spring boot/data jpa/Swagger(OpenAPI)
 * Database: Postgres
 * Mockito(Junit test)
 * Maven 3.6.3

#### Project Structure
    src
      integration-test
      main
      test
   
## Setup

#### Clone the project
   
   
        git clone https://github.com/chandrakumar001/ms-gps-surveys

## Application short overview design

please refer output folder
    
    survey-api-work-flow.png
    survey-er-design.png
    
## Application-URL-Swagger:
  Perform all operation like create, update,delete and show all list.
  
  Local Swagger:
  <http://localhost:8080/swagger-ui.html>
    
    Sample Output: please refer output folder
    
##### Mock database and Data scripts:

    src/main/resources/data.sql
    src/main/resources/schema.sql
    
Reason: 

        H2 DB not supporting jsonb_agg() function, this function
        I used view query for GET call.
 
Please import above script file into local DB as Postgres    
    
##### SQL CREATE VIEW Statement
    
    In SQL, a view is a virtual table based on the result-set of an SQL statement.
    
    A view contains rows and columns, just like a real table. The fields in a view are fields from one or more real tables in the database.
    You can add SQL statements and functions to a view and present the data as if the data were coming from one single table.

#### DTO generation
- [X] Based swagger configuration DTO's will be generated
- [X] Each new swagger file, need to configuration POM file in plug-in section 
    
#### Validation logic
-[x] Incoming request object are validation such null or empty done 
    from swagger file configuration itself
-[X] More Business validation logic create separated package called validation

#### Data validation 
- [X] Each Id validated for UUID format
- [X] Each Id's validated from DB level and Before saving/delete any object


#### CQRS code pattern

Reference: https://medium.com/design-microservices-architecture-with-patterns/cqrs-design-pattern-in-microservices-architectures-5d41e359768c


example:

    Question Object
    constant    :QuestionConstant/QuestionErrorMsgConstant
    validation  : Not used
    controller  :QuestionCommandController/QuestionQueryController
    service/impl:QuestionCommandService/DefaultQuestionCommandService
                :QuestionQueryService/DefaultQuestionQueryService
    mapper      :QuestionCommandMapper/QuestionQueryViewMapper
    entity      :QuestionCommand/QuestionQueryView
    repository  :QuestionCommandRepository/QuestionQueryViewRepository
    DTO(Swagger): At the moment,Not seprated DTO's model.

### Database views(Read operation)
    
   - [X] All GET Endpoints implemented from DB view, in-order to avoid a multiple query fired from DB
   - [X] Views will be performed single query execution and get all data.
   - [X] etc

#### To run Locally
##### Question Object
POST: https://localhost:8080/v1/questions


    {
      "questionTitle": "Which of the following device do you have",
      "answers": [
        {
          "markCorrectAnswer": true,
          "answer": "Smartphone"
        },
        {
          "markCorrectAnswer": false,
          "answer": "Smart TV"
        },
        {
          "markCorrectAnswer": true,
          "answer": "Laptop"
        },
        {
          "markCorrectAnswer": true,
          "answer": "Smart watch"
        }
      ]
    }

output:
    
    {
      "questionId": "5a420559-e201-4463-8f57-bcdbdc3f6369",
      "data": {
        "questionTitle": "Which of the following device do you have",
        "answers": [
          {
            "answerId": "9e8488fd-08f0-4ff3-9824-97d736a8ee12",
            "isCorrectAnswer": true,
            "answerText": "Smartphone"
          },
          {
            "answerId": "7fcba4c0-232b-4c82-b640-40fc75e1ac88",
            "isCorrectAnswer": true,
            "answerText": "Laptop"
          },
          {
            "answerId": "7901d2d8-9766-45cf-a1a5-c40a105102d8",
            "isCorrectAnswer": false,
            "answerText": "Smart TV"
          },
          {
            "answerId": "b8289d59-9f2e-4422-b773-3a1385cb6859",
            "isCorrectAnswer": true,
            "answerText": "Smart watch"
          }
        ]
      },
      "creationDate": "2022-02-21T15:46:46.784966100",
      "updatedDate": "2022-02-21T15:46:46.784966100"
    }

GET   http://localhost:8080/v1/questions/5a420559-e201-4463-8f57-bcdbdc3f6369

Get specific question by using question id

output
   
    {
      "count": 3,
      "questions": [
        {
          "questionId": "5129a0c0-00f9-453f-a886-3605c108fe12",
          "data": {
            "questionTitle": "string",
            "answers": [
              {
                "answerId": "41fac890-1079-4e26-af10-ccb8a3629d84",
                "isCorrectAnswer": true,
                "answerText": "string"
              }
            ]
          },
          "creationDate": "2022-02-20T17:35:20.077217",
          "updatedDate": "2022-02-20T17:35:20.077217"
        },
        {
          "questionId": "3d95f107-c6f9-4b32-90db-94c8de365ff1",
          "data": {
            "questionTitle": "string",
            "answers": [
              {
                "answerId": "be90e321-d8bc-412d-91e4-f9c103e53f9b",
                "isCorrectAnswer": true,
                "answerText": "string"
              }
            ]
          },
          "creationDate": "2022-02-20T17:36:01.106311",
          "updatedDate": "2022-02-20T17:36:01.106311"
        },
        {
          "questionId": "5a420559-e201-4463-8f57-bcdbdc3f6369",
          "data": {
            "questionTitle": "Which of the following device do you have",
            "answers": [
              {
                "answerId": "9e8488fd-08f0-4ff3-9824-97d736a8ee12",
                "isCorrectAnswer": true,
                "answerText": "Smartphone"
              },
              {
                "answerId": "7901d2d8-9766-45cf-a1a5-c40a105102d8",
                "isCorrectAnswer": false,
                "answerText": "Smart TV"
              },
              {
                "answerId": "7fcba4c0-232b-4c82-b640-40fc75e1ac88",
                "isCorrectAnswer": true,
                "answerText": "Laptop"
              },
              {
                "answerId": "b8289d59-9f2e-4422-b773-3a1385cb6859",
                "isCorrectAnswer": true,
                "answerText": "Smart watch"
              }
            ]
          },
          "creationDate": "2022-02-21T15:46:46.784966",
          "updatedDate": "2022-02-21T15:46:46.784966"
        }
      ]
    }
    
    
GET http://localhost:8080/v1/questions

Get all questions which are question created 

output:

        {
          "count": 3,
          "questions": [
            {
              "questionId": "5129a0c0-00f9-453f-a886-3605c108fe12",
              "data": {
                "questionTitle": "string",
                "answers": [
                  {
                    "answerId": "41fac890-1079-4e26-af10-ccb8a3629d84",
                    "isCorrectAnswer": true,
                    "answerText": "string"
                  }
                ]
              },
              "creationDate": "2022-02-20T17:35:20.077217",
              "updatedDate": "2022-02-20T17:35:20.077217"
            },
            {
              "questionId": "3d95f107-c6f9-4b32-90db-94c8de365ff1",
              "data": {
                "questionTitle": "string",
                "answers": [
                  {
                    "answerId": "be90e321-d8bc-412d-91e4-f9c103e53f9b",
                    "isCorrectAnswer": true,
                    "answerText": "string"
                  }
                ]
              },
              "creationDate": "2022-02-20T17:36:01.106311",
              "updatedDate": "2022-02-20T17:36:01.106311"
            },
            {
              "questionId": "5a420559-e201-4463-8f57-bcdbdc3f6369",
              "data": {
                "questionTitle": "Which of the following device do you have",
                "answers": [
                  {
                    "answerId": "9e8488fd-08f0-4ff3-9824-97d736a8ee12",
                    "isCorrectAnswer": true,
                    "answerText": "Smartphone"
                  },
                  {
                    "answerId": "7901d2d8-9766-45cf-a1a5-c40a105102d8",
                    "isCorrectAnswer": false,
                    "answerText": "Smart TV"
                  },
                  {
                    "answerId": "7fcba4c0-232b-4c82-b640-40fc75e1ac88",
                    "isCorrectAnswer": true,
                    "answerText": "Laptop"
                  },
                  {
                    "answerId": "b8289d59-9f2e-4422-b773-3a1385cb6859",
                    "isCorrectAnswer": true,
                    "answerText": "Smart watch"
                  }
                ]
              },
              "creationDate": "2022-02-21T15:46:46.784966",
              "updatedDate": "2022-02-21T15:46:46.784966"
            }
          ]
        }  

Delete: http://localhost:8080/v1/questions/5a420559-e201-4463-8f57-bcdbdc3f6369

Delete specific question by using question Id
     
     refer output folder :: "delete-question.PNG"


##### Survey Object
POST http://localhost:8080/v1/surveys

create survey object

    {
      "title": "Testing My Survey",
      "description": "Testing some description",
      "status": "ACTIVE"
    }        
        
output:
        
        {
          "surveyId": "19aed461-00ee-421a-be59-fd96b8dd4ed9",
          "data": {
            "surveyTitle": "Testing My Survey",
            "description": "Testing some description",
            "status": "ACTIVE"
          },
          "creationDate": "2022-02-21T15:53:38.299000700",
          "updatedDate": "2022-02-21T15:53:38.299000700"
        }


GET http://localhost:8080/v1/surveys/19aed461-00ee-421a-be59-fd96b8dd4ed9

Get specific surveys by using question Id
        
        {
          "survey": {
            "surveyId": "19aed461-00ee-421a-be59-fd96b8dd4ed9",
            "data": {
              "surveyTitle": "Testing My Survey",
              "description": "Testing some description",
              "status": "ACTIVE"
            },
            "creationDate": "2022-02-21T15:53:38.299001",
            "updatedDate": "2022-02-21T15:53:38.299001"
          },
          "questionCount": 0,
          "questions": []
        }
        

DELETE http://localhost:8080/v1/surveys/19aed461-00ee-421a-be59-fd96b8dd4ed9

Delete specific surveys by using question Id

 
       refer output folder :: "delete-survey-object.PNG"

Get http://localhost:8080/v1/surveys/19aed461-00ee-421a-be59-fd96b8dd4ed9/responses

Get an active survey for response
    
    {
      "survey": {
        "surveyId": "19aed461-00ee-421a-be59-fd96b8dd4ed9",
        "data": {
          "surveyTitle": "Testing My Survey",
          "description": "Testing some description",
          "status": "ACTIVE"
        },
        "creationDate": "2022-02-21T15:53:38.299001",
        "updatedDate": "2022-02-21T15:53:38.299001"
      },
      "questionCount": 0,
      "questions": []
    }

##### survey object assign all question
 
POST http://localhost:8080/v1/surveys/19aed461-00ee-421a-be59-fd96b8dd4ed9/questions-assignment

In order to assign all question to survey object
at the moment this endpoint will fetch all question and tag into particular survey object
###### TODO: future, we can accept the by passing selected question list

Just ensure that survey linked all question by using below endpoint

GET http://localhost:8080/v1/surveys/19aed461-00ee-421a-be59-fd96b8dd4ed9/responses


            {
              "survey": {
                "surveyId": "19aed461-00ee-421a-be59-fd96b8dd4ed9",
                "data": {
                  "surveyTitle": "Testing My Survey",
                  "description": "Testing some description",
                  "status": "ACTIVE"
                },
                "creationDate": "2022-02-21T15:53:38.299001",
                "updatedDate": "2022-02-21T15:53:38.299001"
              },
              "questionCount": 1,
              "questions": [
                {
                  "questionId": "5a420559-e201-4463-8f57-bcdbdc3f6369",
                  "data": {
                    "text": "Which of the following device do you have",
                    "answers": [
                      {
                        "answerId": "b8289d59-9f2e-4422-b773-3a1385cb6859",
                        "answerText": "Smart watch"
                      },
                      {
                        "answerId": "7fcba4c0-232b-4c82-b640-40fc75e1ac88",
                        "answerText": "Laptop"
                      },
                      {
                        "answerId": "7901d2d8-9766-45cf-a1a5-c40a105102d8",
                        "answerText": "Smart TV"
                      },
                      {
                        "answerId": "9e8488fd-08f0-4ff3-9824-97d736a8ee12",
                        "answerText": "Smartphone"
                      }
                    ]
                  }
                }
              ]
            }

##### Responses object
POST   http://localhost:8080/v1/surveys/19aed461-00ee-421a-be59-fd96b8dd4ed9/responses

The active survey, we can post the response object which is noting but end user action
         
        
        {
          "questions": [
            {
              "questionId": "5129a0c0-00f9-453f-a886-3605c108fe12",
              "answers": [
                {
                  "answerId": "41fac890-1079-4e26-af10-ccb8a3629d84",
                  "isSelectedAnswer": true
                }
              ]
            }
          ]
        }

output:
            
            {
              "surveyResponseId": "7692560b-e05c-467a-aa16-48e8715fb5a9",
              "message": "The survey has been completed"
            }
    


GET http://localhost:8080/v1/surveys/19aed461-00ee-421a-be59-fd96b8dd4ed9/responses/7692560b-e05c-467a-aa16-48e8715fb5a9/summary
   
  - [x] The endpoint will be provided the result summary 

            
        {
          "summary": {
            "totalCorrectAnswer": 1,
            "totalIncorrectAnswer": 0,
            "totalQuestion": 1
          },
          "questions": [
            {
              "questionId": "5129a0c0-00f9-453f-a886-3605c108fe12",
              "data": {
                "questionTitle": "string",
                "answers": [
                  {
                    "answerId": "41fac890-1079-4e26-af10-ccb8a3629d84",
                    "isSelectedAnswer": true,
                    "isCorrectAnswer": true,
                    "answerStatus": "CORRECT_ANSWER",
                    "answerText": "string"
                  }
                ]
              }
            }
          ]
        }    
#### Error Message
    
    src/main/resources/messages.properties
        
    
## Cloudfoundry

    ibmcloud login -a https://cloud.ibm.com -u passcode -p <passcode>
    ibmcloud target --cf
    ibmcloud cf push  -f cloudfoundry/manifest.yml  --vars-file cloudfoundry/dev-vars.yml

## Kubernetes
    
  At first time, application setup execture: 'kubectl-execute.bat'
 -[x] TODO Kubernetes deployment   
 
####   SonarQube:
    
   Code quality for application
    
        http://localhost:9000/projects
    
##### Application Name:

     ms       --> Means Microservice
     ecom     --> Means Project Name
     gps      --> Generanl Purpose of system(Grouping the funtionalities)   
     Question --> Application Name
     
     Example: ms-gps-surveys     
  
  
##### Spring data JPA:

Soft Delete:(Not Used)
    
    It means that you are flagging a record as deleted in a particular table, instead of actually being deleting the record. 
    
Hard Delete:(Used) 
    
    It means you are completely removing the record from the table        

##### Application Scaling:

Scaling Horizontally:

Incoming requests to your application are automatically load balanced across all instances of your application, and each instance handles tasks in parallel with every other instance. 

    ibmcloud cf scale ms-gps-surveys -i 2

Scaling Vertically:

Vertically scaling an application changes the disk space limit or memory limit that Cloud Foundry applies to all instances of the application


-k DISK to change the disk space limit applied to all instances of your application

-m MEMORY must be an integer followed by either an M, for megabytes, or G, for gigabytes


    ibmcloud cf scale ms-gps-surveys -k 512M
    ibmcloud cf scale ms-gps-surveys -m 1G
    
Show all apps:
        
        ibmcloud cf ms-gps-surveys
        ibmcloud cf logs ms-gps-surveys
        
##### Request tracking and logging by using below component 

logging: Clean up activity for before request and after request by using mdc approach 

Components | ORDER
-----------| -------------
logging    | 0
tracking   | 1


#### Database design
    
    Three type object, ensuring ownership of object I used database schema level segregation

    -[X] schema:  survey, responses and questions
    -[X] views: survey.survey_responses_query_view, survey.survey_question_query_view and questions.question_query_view
    