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

## 6) Delivery Plan

### Milestone 1 - Foundation

- Finalize package structure
- Create entities, enums, repositories
- Configure PostgreSQL and environments

### Milestone 2 - Core APIs

- Implement Freelancer, Job, Comment CRUD flows
- Add filter/search endpoint for freelancers
- Implement update paths with validation

### Milestone 3 - Async Evaluation + Quality

- Mock async evaluation on freelancer create
- Add unit/integration tests for key flows
- Add Swagger docs + Postman collection

### Milestone 4 - Delivery

- Final README and run guide
- Docker support (optional)
- GitHub submission package

## 7) Risks and Mitigation

- **Risk:** unclear search semantics for multi-filter queries  
  **Mitigation:** define exact matching/contains behavior before coding
- **Risk:** async score expectations unclear to business users  
  **Mitigation:** expose score status field and demo lifecycle in API docs
- **Risk:** environment setup friction (Java/Maven/DB)  
  **Mitigation:** use Maven wrapper and provide one-command startup guide
