# Odine Freelance Marketplace API (Demo Blueprint)

This repository is currently a **demo + thin-implementation version** of the backend product.
Its purpose is to communicate target architecture and show a working vertical slice before full implementation.

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
- JPA/Hibernate + validation

## Run Modes

- **Demo mode (default):** boots with in-memory H2 for stakeholder demos and smoke tests
- **PostgreSQL mode:** for implementation phase with persistence enabled

### Quick Start (Windows PowerShell)

From project root:

- Demo startup: `.\run-demo.ps1`
- PostgreSQL startup: `.\run-demo.ps1 -Profile postgres`

The script:

- uses Maven wrapper (no global `mvn` required)
- resolves a local JDK from `javac` and sets `JAVA_HOME` for the session

## Implemented Thin Slice

- Domain model and relations:
  - `Freelancer (1) -> (N) Job`
  - `Job (1) -> (N) Comment`
- Freelancer async evaluation flow:
  - create with `PENDING`
  - background score computation
  - update to `COMPLETED`/`FAILED`
- Working endpoints:
  - `POST /api/v1/freelancers`
  - `GET /api/v1/freelancers/{freelancerId}`
  - `GET /api/v1/freelancers`
  - `GET /api/v1/freelancers/search`
- Consistent error shape via global exception handler

## Proposed Documentation

- `docs/DEMO_PRODUCT_BRIEF.md`: stakeholder-facing product details and roadmap
- `docs/PROJECT_STRUCTURE.md`: proposed folder/package structure

## Next Step After Approval

1. Complete Job and Comment API vertical slices
2. Add unit/integration tests for current endpoints
3. Add Swagger + Postman collection
4. Finalize PostgreSQL profile run instructions and delivery README
