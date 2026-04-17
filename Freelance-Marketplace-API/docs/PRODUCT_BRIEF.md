# Product Brief

## 1) Vision

Odine Freelance Marketplace API enables freelancer profiles, job records, and job comments through a robust REST backend.
The system is designed to be simple to integrate and easy to extend for future marketplace features.

## 2) Stakeholder-Focused Outcome

This brief explains:

- What product will be delivered
- Which features are in scope
- How data and workflows will operate
- How implementation will be phased and de-risked

## 3) Functional Scope

### Freelancer Domain

- Create freelancer (designer or software developer)
- List freelancers
- Get freelancer by ID
- Search freelancers by:
  - name
  - city
  - type
  - design tool
  - specialty

### Job Domain

- Create job for a freelancer
- List jobs by freelancer
- Get job by ID
- Update job status and description

### Comment Domain

- Create comment for a job
- List comments by job
- Update comment text

## 4) Core Business Rules

- Freelancer types:
  - `DESIGNER`
  - `SOFTWARE_DEVELOPER`
- Designer required fields:
  - portfolio URL
  - design tools known
- Developer required fields:
  - software languages known
  - specialties
- Async evaluation score triggered on freelancer creation:
  - Designer score = number of tools known
  - Developer score = number of specialties * number of languages
  - Score normalized to range `1..10`

## 5) Implementation Order (What We Did, Step by Step)

1. Bootstrapped the Spring Boot project with Java 17, Maven Wrapper, and PostgreSQL profile setup.
2. Modeled the core entities and relationships: `Freelancer (1) -> (N) Job`, `Job (1) -> (N) Comment`.
3. Implemented Freelancer APIs first (create/list/get/search) with type-specific validation rules.
4. Added asynchronous freelancer evaluation flow and status tracking (`PENDING`, `COMPLETED`, `FAILED`).
5. Implemented Job APIs (create/list by freelancer/get/update) and Comment APIs (create/list by job/update).
6. Added consistent API error handling through a global exception handler and shared error response model.
7. Added API documentation and API tooling (Swagger/OpenAPI configuration and Postman collection).
8. Upgraded async processing to RabbitMQ message queue consumer flow.
9. Dockerized the stack with `app + postgres + rabbitmq` in `docker-compose.yml`.

## 6) Current State Summary

- Core scope is implemented for Freelancer, Job, and Comment domains.
- API is stateless, versioned under `/api/v1`, and documented with Swagger.
- Freelancer evaluation runs asynchronously via RabbitMQ consumer processing.
- Project can run locally with Docker Compose, including database and message broker.
