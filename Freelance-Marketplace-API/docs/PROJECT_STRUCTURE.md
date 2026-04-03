# Proposed Project Structure (Demo)

```text
Freelance-Marketplace-API/
  src/
    main/
      java/com/odine/freelance/marketplace/api/
        config/
          AsyncConfig.java
          OpenApiConfig.java
        common/
          exception/
            GlobalExceptionHandler.java
            NotFoundException.java
            ValidationException.java
          response/
            ApiErrorResponse.java
        freelancer/
          controller/
            FreelancerController.java
          dto/
            FreelancerCreateRequest.java
            FreelancerResponse.java
            FreelancerSearchRequest.java
          entity/
            Freelancer.java
            FreelancerType.java
            EvaluationStatus.java
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
        application-dev.properties
        application-prod.properties
    test/
      java/com/odine/freelance/marketplace/api/
        freelancer/FreelancerControllerTest.java
        job/JobControllerTest.java
        comment/CommentControllerTest.java
  docs/
    DEMO_PRODUCT_BRIEF.md
    API_CONTRACT_DRAFT.md
    PROJECT_STRUCTURE.md
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
