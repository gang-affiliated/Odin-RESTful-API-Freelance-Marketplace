# Odine Freelance Marketplace API (Demo Blueprint)

This repository is currently a **demo/planning version** of the backend product.
Its purpose is to communicate the target system to stakeholders before full implementation.

## Product Goal

Build a REST API for a freelance marketplace that supports:

- Freelancer onboarding and discovery
- Job lifecycle tracking
- Job comments and collaboration
- Asynchronous freelancer evaluation scoring

## Demo Scope (Current Phase)

- Define clear functional scope from the coding task
- Show proposed domain model and API surface
- Show project architecture and package structure
- Provide execution plan and delivery milestones

## Planned Business Capabilities

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

- **Demo mode (default):** boots without database for stakeholder demos
- **PostgreSQL mode:** for implementation phase with persistence enabled

### Quick Start (Windows PowerShell)

From project root:

- Demo startup: `.\run-demo.ps1`
- PostgreSQL startup: `.\run-demo.ps1 -Profile postgres`

The script:

- uses Maven wrapper (no global `mvn` required)
- resolves a local JDK from `javac` and sets `JAVA_HOME` for the session

## Proposed Documentation

- `docs/DEMO_PRODUCT_BRIEF.md`: stakeholder-facing product details and roadmap
- `docs/API_CONTRACT_DRAFT.md`: endpoint-level draft contract
- `docs/PROJECT_STRUCTURE.md`: proposed folder/package structure
- `docs/PRESENTATION_ASSETS.md`: data-relation diagram + sample API payloads

## Next Step After Approval

After stakeholder sign-off, we move to implementation sprint:

1. Domain models + database schema
2. Service and controller layers
3. Async mock score processor
4. Validation and error handling
5. Tests, Swagger, Postman collection, and README completion
