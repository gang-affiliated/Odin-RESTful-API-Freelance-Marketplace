# Odine Freelance Marketplace API

A production-grade RESTful backend for a freelance marketplace, built with **Spring Boot 3**, **Java 17**, **PostgreSQL**, and **RabbitMQ**. It exposes clean, versioned HTTP endpoints for freelancer onboarding, job lifecycle tracking, and collaborative job comments — with an asynchronous evaluation pipeline that scores freelancers after they register.

> The project is fully Dockerized, documented via Swagger/OpenAPI, and ships with a ready-to-import Postman collection and automated controller tests.

---

## Table of Contents

1. [Overview](#1-overview)
2. [Key Features](#2-key-features)
3. [Tech Stack](#3-tech-stack)
4. [Architecture](#4-architecture)
5. [Domain Model](#5-domain-model)
6. [Asynchronous Evaluation Flow](#6-asynchronous-evaluation-flow)
7. [Project Structure](#7-project-structure)
8. [Getting Started](#8-getting-started)
9. [Configuration](#9-configuration)
10. [API Reference](#10-api-reference)
11. [Error Handling](#11-error-handling)
12. [Testing](#12-testing)
13. [Tooling & Documentation](#13-tooling--documentation)
14. [Troubleshooting](#14-troubleshooting)
15. [Further Documentation](#15-further-documentation)

---

## 1. Overview

The Odine Freelance Marketplace API is the backend for a platform that connects freelancers (designers and software developers) with clients. It focuses on:

- **Freelancer onboarding and discovery** — registration, lookup, filtered search.
- **Job lifecycle tracking** — create jobs, update status, associate them to freelancers.
- **Collaboration** — thread-free, flat comments attached to individual jobs.
- **Asynchronous scoring** — freelancers get an evaluation score (1–10) produced by a background worker.

The API is stateless, versioned under `/api/v1`, and designed to be easy to integrate with any frontend or third-party consumer.

---

## 2. Key Features

- **Clean REST endpoints** for Freelancers, Jobs, and Comments.
- **Type-specific validation** for designers vs. software developers.
- **Free-text + filter search** over freelancers (name, city, type, tool, specialty).
- **Async evaluation pipeline** via RabbitMQ: create → publish message → consumer computes and persists score.
- **Configurable mock delay** for the evaluation worker to simulate real-world latency.
- **Global exception handler** with a consistent JSON error contract.
- **Swagger/OpenAPI UI** generated automatically from the code.
- **Dockerized stack** (API + PostgreSQL + RabbitMQ) runnable with a single command.
- **Automated tests** on all three controllers covering success and edge cases.
- **Cross-platform scripts** (`run.ps1` for Windows, `run.sh` for Linux/macOS).

---

## 3. Tech Stack

| Layer              | Technology                                    |
| ------------------ | --------------------------------------------- |
| Language           | Java 17                                       |
| Framework          | Spring Boot 3.5.x                             |
| Web                | Spring Web (MVC)                              |
| Persistence        | Spring Data JPA + Hibernate                   |
| Database           | PostgreSQL 16                                 |
| Messaging          | RabbitMQ 3.13 (via Spring AMQP)               |
| Validation         | Jakarta Bean Validation (`spring-boot-starter-validation`) |
| API Docs           | springdoc-openapi (Swagger UI)                |
| Build              | Maven (Maven Wrapper included)                |
| Containerization   | Docker + Docker Compose                       |
| Testing            | JUnit 5, Spring Boot Test, MockMvc (`@WebMvcTest`) |

---

## 4. Architecture

The codebase follows a **feature-per-package, layered architecture**. Each domain (freelancer, job, comment) owns its controller, service, repository, dto, and entity files. Cross-cutting concerns (errors, config) are isolated under `common/` and `config/`.

```
HTTP Request
    |
    v
+-------------+   validates & maps DTO
|  Controller |----------------------------+
+-------------+                            |
    |                                      |
    v                                      v
+-------------+   business logic    +----------------+
|   Service   |-------------------->|   Global Error |
+-------------+                     |     Handler    |
    |                               +----------------+
    v
+-------------+   JPA/Hibernate
| Repository  |----------------->   PostgreSQL
+-------------+
    |
    | (on freelancer creation)
    v
+----------------+  publishes message  +------------+
| RabbitTemplate |-------------------->|  RabbitMQ  |
+----------------+                     +-----+------+
                                            |
                                            v
                                  +----------------------+
                                  | Evaluation Consumer  |
                                  | (async score worker) |
                                  +----------------------+
```

**Layering rules (enforced by package boundaries):**

- `controller` — HTTP transport and validation entry points only.
- `service` — all business logic, orchestration, and transactions.
- `repository` — persistence via Spring Data JPA.
- `dto` — isolates API contracts from entity internals.
- `messaging` — RabbitMQ producers/consumers.
- `common` — shared error responses and global exception handling.
- `config` — framework-level configuration (RabbitMQ topology, OpenAPI metadata).

---

## 5. Domain Model

Three aggregates with the following relationships:

```
Freelancer (1) ------< Job (N) ------< Comment (N)
```

### Freelancer

- Identity: `id`, `name`, `email` (unique), `phone`, `city`.
- Type-specific fields:
  - `DESIGNER` — `portfolioUrl`, `designTools`.
  - `SOFTWARE_DEVELOPER` — `softwareLanguages`, `specialties`.
- Evaluation: `evaluationStatus` (`PENDING` / `COMPLETED` / `FAILED`) and `evaluationScore` (1–10).
- Flexible metadata: `additionalFields` (free-form key/value map).

### Job

- Belongs to exactly one `Freelancer` (`freelancerId`).
- `status`: `IN_PROGRESS` (default) or `FINISHED`.
- `description` (up to 2000 chars).
- `createdDate` set automatically on persist.

### Comment

- Belongs to exactly one `Job` (`jobId`).
- `commenterName`, `comment` text, `createdDate`.

---

## 6. Asynchronous Evaluation Flow

When a freelancer is created, the API immediately returns `201 Created` with status `PENDING`. A background worker computes the score asynchronously.

```
+-----------+   1. POST /freelancers       +------------+
|  Client   |----------------------------->| Freelancer |
+-----------+                              |  Service   |
      ^                                    +-----+------+
      |  2. 201 Created (status=PENDING)         |
      |                                          | 3. publish freelancerId
      |                                          v
      |                                    +------------+
      |                                    |  RabbitMQ  |
      |                                    |  (queue)   |
      |                                    +-----+------+
      |                                          |
      |                                          v
      |                                +--------------------+
      |                                | Evaluation Consumer|
      |                                | (mock delay + calc)|
      |                                +-----+--------------+
      |                                      | 4. update status + score
      |                                      v
      |                                 +------------+
      |  5. GET /freelancers/{id}       | PostgreSQL |
      +-------------------------------->|            |
                                        +------------+
```

### Scoring Rules

| Freelancer Type     | Raw Score                                         | Normalized |
| ------------------- | ------------------------------------------------- | ---------- |
| `DESIGNER`          | `count(designTools)`                              | clamped to `[1, 10]` |
| `SOFTWARE_DEVELOPER`| `count(specialties) * count(softwareLanguages)`   | clamped to `[1, 10]` |

### Mock Delay

The worker sleeps for `freelancer.evaluation.mock-delay-ms` (default **60 000 ms** = 60 s) before computing the score. This simulates real processing latency and is useful for demoing the async flow. Set it to `0` to disable.

### RabbitMQ Topology

- **Exchange:** `freelancer.evaluation.exchange` (direct)
- **Queue:** `freelancer.evaluation.queue` (durable)
- **Routing key:** `freelancer.evaluation`

On any unexpected error during evaluation, the freelancer's status is flipped to `FAILED` and `evaluationScore` is cleared.

---

## 7. Project Structure

```text
Freelance-Marketplace-API/
|- postman/
|  |- Freelance-Marketplace-API.postman_collection.json
|- src/
|  |- main/
|  |  |- java/com/odine/Freelance/Marketplace/API/
|  |  |  |- FreelanceMarketplaceApiApplication.java
|  |  |  |- config/
|  |  |  |  |- OpenApiConfig.java
|  |  |  |  |- RabbitMqConfig.java
|  |  |  |- common/
|  |  |  |  |- exception/GlobalExceptionHandler.java
|  |  |  |  |- response/ApiErrorResponse.java
|  |  |  |- freelancer/
|  |  |  |  |- controller/FreelancerController.java
|  |  |  |  |- dto/               # FreelancerCreateRequest, FreelancerResponse
|  |  |  |  |- entity/            # Freelancer, FreelancerType, EvaluationStatus
|  |  |  |  |- messaging/FreelancerEvaluationConsumer.java
|  |  |  |  |- repository/FreelancerRepository.java
|  |  |  |  |- service/           # FreelancerService, FreelancerEvaluationService
|  |  |  |- job/
|  |  |  |  |- controller/JobController.java
|  |  |  |  |- dto/               # JobCreateRequest, JobUpdateRequest, JobResponse
|  |  |  |  |- entity/            # Job, JobStatus
|  |  |  |  |- repository/JobRepository.java
|  |  |  |  |- service/JobService.java
|  |  |  |- comment/
|  |  |     |- controller/CommentController.java
|  |  |     |- dto/               # CommentCreateRequest, CommentUpdateRequest, CommentResponse
|  |  |     |- entity/Comment.java
|  |  |     |- repository/CommentRepository.java
|  |  |     |- service/CommentService.java
|  |  |- resources/
|  |     |- application.properties
|  |     |- application-postgres.properties
|  |- test/
|     |- java/.../controller/       # @WebMvcTest coverage for all controllers
|- docker-compose.yml                 # app + postgres + rabbitmq
|- Dockerfile                         # multi-stage build (maven -> temurin JRE)
|- mvnw / mvnw.cmd                    # Maven Wrapper
|- run.sh                             # Linux/macOS startup helper
|- run.ps1                            # Windows startup helper
|- pom.xml
|- README.md
```

---

## 8. Getting Started

### 8.1 Prerequisites

Choose **one** of the following setups:

| Setup              | Requirements                                           |
| ------------------ | ------------------------------------------------------ |
| Docker (recommended) | Docker Desktop (with Docker Compose v2)              |
| Local (native)     | Java 17+ (`javac` on PATH), PostgreSQL 14+, RabbitMQ 3.x |

No global Maven install is required — the project ships the Maven Wrapper (`mvnw` / `mvnw.cmd`).

### 8.2 Quick Start with Docker (recommended)

From the project root:

```bash
docker compose up --build
```

This spins up three containers with healthchecks wired correctly:

| Service        | URL                                    | Credentials        |
| -------------- | -------------------------------------- | ------------------ |
| API            | http://localhost:8080                  | -                  |
| Swagger UI     | http://localhost:8080/swagger-ui/index.html | -             |
| PostgreSQL     | `localhost:5432` (db: `freelance_marketplace`) | `postgres` / `postgres` |
| RabbitMQ AMQP  | `localhost:5672`                       | `guest` / `guest`  |
| RabbitMQ UI    | http://localhost:15672                 | `guest` / `guest`  |

Stop and clean up:

```bash
docker compose down            # stop containers
docker compose down -v         # also remove postgres_data / rabbitmq_data volumes
```

### 8.3 Quick Start on Linux / macOS

```bash
./run.sh
```

The script:

- resolves the local JDK from `javac` and exports `JAVA_HOME` for the session
- runs the app via the Maven wrapper with the `postgres` profile

You still need a local PostgreSQL and RabbitMQ reachable at the default ports (or set the env vars in [Configuration](#9-configuration)).

### 8.4 Quick Start on Windows (PowerShell)

```powershell
.\run.ps1
```

Same behavior as `run.sh`, tailored for PowerShell.

### 8.5 Manual Maven Run

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

---

## 9. Configuration

All properties are resolved from environment variables with sensible defaults (see `src/main/resources/application-postgres.properties`):

| Property                                | Env Var                    | Default                                              |
| --------------------------------------- | -------------------------- | ---------------------------------------------------- |
| `spring.datasource.url`                 | `DB_URL`                   | `jdbc:postgresql://localhost:5432/freelance_marketplace` |
| `spring.datasource.username`            | `DB_USERNAME`              | `postgres`                                           |
| `spring.datasource.password`            | `DB_PASSWORD`              | `postgres`                                           |
| `spring.rabbitmq.host`                  | `RABBITMQ_HOST`            | `localhost`                                          |
| `spring.rabbitmq.port`                  | `RABBITMQ_PORT`            | `5672`                                               |
| `spring.rabbitmq.username`              | `RABBITMQ_USERNAME`        | `guest`                                              |
| `spring.rabbitmq.password`              | `RABBITMQ_PASSWORD`        | `guest`                                              |
| `freelancer.evaluation.mock-delay-ms`   | `EVALUATION_MOCK_DELAY_MS` | `60000` (60 s)                                       |

Other defaults worth knowing:

- `spring.jpa.hibernate.ddl-auto=update` — schema is auto-created/updated at startup.
- `spring.jpa.open-in-view=false` — safer transactional semantics.
- `spring.jpa.show-sql=true` and `format_sql=true` — SQL is logged for debugging.

> **Tip:** To see the async flow instantly during a demo, set `EVALUATION_MOCK_DELAY_MS=0` (or `2000` for a short but visible delay).

---

## 10. API Reference

**Base URL:** `http://localhost:8080`
**All endpoints are versioned under `/api/v1`.**

A ready-to-import Postman collection lives at `postman/Freelance-Marketplace-API.postman_collection.json`.

### 10.1 Freelancers

| Method | Path                                   | Description                                   |
| ------ | -------------------------------------- | --------------------------------------------- |
| POST   | `/api/v1/freelancers`                  | Create a freelancer (triggers async evaluation) |
| GET    | `/api/v1/freelancers`                  | List all freelancers                          |
| GET    | `/api/v1/freelancers/{freelancerId}`   | Get a single freelancer                       |
| GET    | `/api/v1/freelancers/search`           | Search with optional filters                  |

**Search query params** (all optional, combinable): `name`, `city`, `type` (`DESIGNER` or `SOFTWARE_DEVELOPER`), `tool`, `specialty`.

**Example — create a software developer:**

```bash
curl -X POST http://localhost:8080/api/v1/freelancers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mert Kaya",
    "email": "mert.kaya@example.com",
    "phone": "+90-555-000-3344",
    "city": "Ankara",
    "freelancerType": "SOFTWARE_DEVELOPER",
    "softwareLanguages": ["Java", "Python"],
    "specialties": ["Backend", "API"]
  }'
```

**Response (`201 Created`):**

```json
{
  "id": 1,
  "name": "Mert Kaya",
  "email": "mert.kaya@example.com",
  "freelancerType": "SOFTWARE_DEVELOPER",
  "evaluationStatus": "PENDING",
  "evaluationScore": null,
  "softwareLanguages": ["Java", "Python"],
  "specialties": ["Backend", "API"]
}
```

After the mock delay, `GET /api/v1/freelancers/1` will show `"evaluationStatus": "COMPLETED"` with an `evaluationScore` in `[1, 10]`.

### 10.2 Jobs

| Method | Path                                           | Description                          |
| ------ | ---------------------------------------------- | ------------------------------------ |
| POST   | `/api/v1/jobs`                                 | Create a job for a freelancer        |
| GET    | `/api/v1/freelancers/{freelancerId}/jobs`      | List jobs belonging to a freelancer  |
| GET    | `/api/v1/jobs/{jobId}`                         | Get a single job                     |
| PATCH  | `/api/v1/jobs/{jobId}`                         | Update status and/or description     |

**Example — create a job:**

```bash
curl -X POST http://localhost:8080/api/v1/jobs \
  -H "Content-Type: application/json" \
  -d '{
    "freelancerId": 1,
    "status": "IN_PROGRESS",
    "description": "Landing page redesign and component handoff"
  }'
```

**Example — mark job finished:**

```bash
curl -X PATCH http://localhost:8080/api/v1/jobs/1 \
  -H "Content-Type: application/json" \
  -d '{ "status": "FINISHED", "description": "Redesign completed and delivered" }'
```

### 10.3 Comments

| Method | Path                                          | Description                     |
| ------ | --------------------------------------------- | ------------------------------- |
| POST   | `/api/v1/jobs/{jobId}/comments`               | Add a comment to a job          |
| GET    | `/api/v1/jobs/{jobId}/comments`               | List comments for a job         |
| PATCH  | `/api/v1/comments/{commentId}`                | Update a comment's text         |

**Example:**

```bash
curl -X POST http://localhost:8080/api/v1/jobs/1/comments \
  -H "Content-Type: application/json" \
  -d '{ "commenterName": "ProjectOwner", "comment": "Great delivery speed and quality" }'
```

---

## 11. Error Handling

All errors are serialized to a consistent JSON envelope by `GlobalExceptionHandler`:

```json
{
  "timestamp": "2026-04-23T21:10:15",
  "status": 400,
  "error": "Bad Request",
  "message": "email: must be a well-formed email address",
  "path": "/api/v1/freelancers"
}
```

| Scenario                                          | HTTP Status | Typical Trigger                                  |
| ------------------------------------------------- | ----------- | ------------------------------------------------ |
| Validation failure (`@Valid`)                     | `400`       | Missing/invalid field in request body            |
| Unknown enum value / malformed JSON               | `400`       | `freelancerType` set to an unsupported value     |
| Resource not found                                | `404`       | Unknown `freelancerId`, `jobId`, or `commentId`  |
| Conflict (e.g., duplicate email)                  | `409`       | Creating a freelancer with an existing email     |
| Unhandled server error                            | `500`       | Unexpected exception (never exposes stack trace) |

---

## 12. Testing

Controller-level tests use `@WebMvcTest` + MockMvc and cover happy paths and edge cases:

- `FreelancerControllerTest` — create success, validation error, duplicate-email conflict.
- `JobControllerTest` — create success, update success, invalid update request.
- `CommentControllerTest` — create success, validation error, update success.

Run the suite:

```bash
# macOS / Linux
./mvnw test

# Windows
.\mvnw.cmd test
```

> If Maven complains about a JRE/JDK mismatch, run `./run.sh` (or `.\run.ps1`) once — it resolves `JAVA_HOME` for the session — then re-run the test command in the same terminal.

---

## 13. Tooling & Documentation

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **Postman collection:** `postman/Freelance-Marketplace-API.postman_collection.json`
  - Import it into Postman; it pre-defines `baseUrl`, `freelancerId`, `jobId`, `commentId` variables so you can wire requests together quickly.
- **RabbitMQ Management UI:** http://localhost:15672 — inspect the `freelancer.evaluation.queue` and watch messages flow during a demo.

---

## 14. Troubleshooting

| Symptom                                            | Likely Cause / Fix                                                                                  |
| -------------------------------------------------- | --------------------------------------------------------------------------------------------------- |
| `Connection refused` to PostgreSQL at `5432`       | Start the DB with `docker compose up postgres`, or point `DB_URL` to your instance.                 |
| `Connection refused` to RabbitMQ at `5672`         | Start it with `docker compose up rabbitmq`, or set `RABBITMQ_HOST` / `RABBITMQ_PORT`.               |
| Freelancer stays `PENDING` forever                 | Check the consumer is running (`RabbitMQ UI -> Queues`). Also verify `EVALUATION_MOCK_DELAY_MS`.     |
| `Port 8080 already in use`                         | Stop the conflicting process or change the mapped port in `docker-compose.yml`.                    |
| `JAVA_HOME is not set to a JDK`                    | Install JDK 17+ and ensure `javac` is on `PATH`. The `run.sh` / `run.ps1` scripts auto-resolve it. |
| Tests fail with a JRE/JDK mismatch                 | Use `run.sh` / `run.ps1` first in the same shell, then run `./mvnw test`.                           |
| `duplicate key value violates unique constraint`   | Email is already used — pick another one or delete the existing record.                            |

---

## 15. Further Documentation

- [`postman/Freelance-Marketplace-API.postman_collection.json`](postman/Freelance-Marketplace-API.postman_collection.json) — end-to-end request collection.
