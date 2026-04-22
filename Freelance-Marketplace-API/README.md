# Odine Freelance Marketplace API

This repository contains the backend implementation for the Odine Freelance Marketplace API.

## Product Goal

Build a REST API for a freelance marketplace that supports:

- Freelancer onboarding and discovery
- Job lifecycle tracking
- Job comments and collaboration
- Asynchronous freelancer evaluation scoring

## Scope

- **Freelancer management:** create, list, view, and search freelancers
- **Job management:** create and update jobs linked to freelancers
- **Comment management:** add and manage comments linked to jobs
- **Evaluation pipeline:** mock async scoring process triggered on freelancer creation

## Technical Baseline

- Java 17+
- Spring Boot 3+
- Maven Wrapper (`mvnw.cmd`)
- PostgreSQL
- RabbitMQ
- JPA/Hibernate + validation
- Swagger/OpenAPI (springdoc)

## Run Profiles

- **PostgreSQL profile (default):** main runtime profile for persistent storage

### Quick Start (Windows PowerShell)

From project root:

- Startup: `.\run.ps1`

The script:

- uses Maven wrapper (no global `mvn` required)
- resolves a local JDK from `javac` and sets `JAVA_HOME` for the session

### Quick Start (Linux/macOS)

From project root:

- Startup: `./run.sh`

Script behavior:

- uses Maven wrapper (`./mvnw`)
- resolves local JDK from `javac` and sets `JAVA_HOME` for the shell session

### Quick Start with Docker (Recommended for reviewers)

From project root:

- Start all services: `docker compose up --build`
- Stop all services: `docker compose down`

Services started by compose:

- API: `http://localhost:8080`
- PostgreSQL: `localhost:5432`
- RabbitMQ broker: `localhost:5672`
- RabbitMQ management UI: `http://localhost:15672` (`guest` / `guest`)

### API Documentation (Swagger)

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Implemented Features

- Domain model and relations:
  - `Freelancer (1) -> (N) Job`
  - `Job (1) -> (N) Comment`
- Freelancer async evaluation flow:
  - create with `PENDING`
  - publish evaluation message to RabbitMQ
  - wait on configurable mock delay (`freelancer.evaluation.mock-delay-ms`, default 60000 ms)
  - background score computation by queue consumer
  - update to `COMPLETED`/`FAILED`
- Working endpoints:
  - `POST /api/v1/freelancers`
  - `GET /api/v1/freelancers/{freelancerId}`
  - `GET /api/v1/freelancers`
  - `GET /api/v1/freelancers/search`
  - `POST /api/v1/jobs`
  - `GET /api/v1/freelancers/{freelancerId}/jobs`
  - `GET /api/v1/jobs/{jobId}`
  - `PATCH /api/v1/jobs/{jobId}`
  - `POST /api/v1/jobs/{jobId}/comments`
  - `GET /api/v1/jobs/{jobId}/comments`
  - `PATCH /api/v1/comments/{commentId}`
- Consistent error shape via global exception handler

## Automated Tests

- Added controller-level test coverage with `@WebMvcTest` for:
  - `FreelancerController` (create success, validation error, duplicate email conflict)
  - `JobController` (create success, update success, invalid update request)
  - `CommentController` (create success, validation error, update success)
- Run tests:
  - `.\mvnw.cmd test`
- If Maven reports JRE/JDK mismatch:
  - run `.\run.ps1` once (it resolves and sets `JAVA_HOME`), stop the app with `Ctrl+C`, then run `.\mvnw.cmd test`

## Project Documentation

- `docs/PRODUCT_BRIEF.md`: scope and implementation order summary
- `docs/PROJECT_STRUCTURE.md`: package layout and layering notes
- `postman/Freelance-Marketplace-API.postman_collection.json`: ready-to-import Postman collection
