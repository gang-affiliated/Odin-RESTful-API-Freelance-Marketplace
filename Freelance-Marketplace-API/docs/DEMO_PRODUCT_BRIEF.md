# Demo Product Brief

## 1) Vision

Odine Freelance Marketplace API enables freelancer profiles, job records, and job comments through a robust REST backend.
The system is designed to be simple to integrate and easy to extend for future marketplace features.

## 2) Stakeholder-Focused Outcome

This demo explains:

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

## 5) Proposed Non-Functional Qualities

- Input validation for all write endpoints
- Consistent API error payloads
- Search endpoints optimized for common filters
- Audit-friendly timestamps for job and comment entities
- Separation of concerns (controller/service/repository layers)
- Explicit relationship modeling in JPA entities, while keeping normalized tables and foreign keys at database level

## 6) Delivery Plan

The first delivery increment includes finalized domain modeling for Freelancer, Job, and Comment plus a working Freelancer API slice.
This slice demonstrates create/get/list/search flows, validation rules, and asynchronous evaluation status updates.

The next increment completes Job and Comment endpoints with the same service/controller/error-handling approach.

Final packaging will include tests, Swagger/Postman artifacts, and submission-ready project documentation.
