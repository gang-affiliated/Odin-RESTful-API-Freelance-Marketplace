# Proposed Project Structure

```text
Freelance-Marketplace-API/
  src/
    main/
      java/com/odine/freelance/marketplace/api/
        config/
          RabbitMqConfig.java
          OpenApiConfig.java
        common/
          exception/
            GlobalExceptionHandler.java
          response/
            ApiErrorResponse.java
        freelancer/
          controller/
            FreelancerController.java
          dto/
            FreelancerCreateRequest.java
            FreelancerResponse.java
          entity/
            Freelancer.java
            FreelancerType.java
            EvaluationStatus.java
          messaging/
            FreelancerEvaluationConsumer.java
          repository/
            FreelancerRepository.java
          service/
            FreelancerService.java
            FreelancerEvaluationService.java
        job/
          controller/
            JobController.java
          dto/
            JobCreateRequest.java
            JobUpdateRequest.java
            JobResponse.java
          entity/
            Job.java
            JobStatus.java
          repository/
            JobRepository.java
          service/
            JobService.java
        comment/
          controller/
            CommentController.java
          dto/
            CommentCreateRequest.java
            CommentUpdateRequest.java
            CommentResponse.java
          entity/
            Comment.java
          repository/
            CommentRepository.java
          service/
            CommentService.java
        FreelanceMarketplaceApiApplication.java
      resources/
        application.properties
        application-postgres.properties
    test/
      java/com/odine/freelance/marketplace/api/
        freelancer/controller/FreelancerControllerTest.java
        job/controller/JobControllerTest.java
        comment/controller/CommentControllerTest.java
  docs/
    PRODUCT_BRIEF.md
    PROJECT_STRUCTURE.md
  postman/
    Freelance-Marketplace-API.postman_collection.json
  docker-compose.yml
  Dockerfile
  pom.xml
  README.md
```

## Layering Rules

- `controller` only handles HTTP transport and validation entry points
- `service` contains all business logic and orchestration
- `repository` handles persistence with Spring Data JPA
- `dto` isolates API contracts from entity internals
- `common` centralizes cross-cutting concerns (errors, shared responses)

## Data Ownership

- `Freelancer` is a root aggregate for profile data
- `Job` belongs to one `Freelancer`
- `Comment` belongs to one `Job`

## Modeling Decision

- Used entity references for relationship modeling (`Job -> Freelancer`, `Comment -> Job`) to keep cardinality explicit in code while still persisting normalized foreign keys in separate tables.

## Current Implementation

- Implemented:
  - `freelancer/controller/FreelancerController`
  - `job/controller/JobController`
  - `comment/controller/CommentController`
  - `freelancer/dto/FreelancerCreateRequest`, `FreelancerResponse`
  - `job/dto/JobCreateRequest`, `JobUpdateRequest`, `JobResponse`
  - `comment/dto/CommentCreateRequest`, `CommentUpdateRequest`, `CommentResponse`
  - `freelancer/service/FreelancerService`, `FreelancerEvaluationService`
  - `job/service/JobService`
  - `comment/service/CommentService`
  - `common/exception/GlobalExceptionHandler`
  - `common/response/ApiErrorResponse`
  - `freelancer/messaging/FreelancerEvaluationConsumer`
  - `config/RabbitMqConfig`, `config/OpenApiConfig`
  - Dockerized runtime with `docker-compose.yml` and `Dockerfile`
  - controller tests for freelancer/job/comment endpoints

