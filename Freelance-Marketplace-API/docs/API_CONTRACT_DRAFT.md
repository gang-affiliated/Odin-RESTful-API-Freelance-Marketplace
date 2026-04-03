# API Contract Draft (Demo)

Base path: `/api/v1`

## How This Is RESTful

- Resources are represented as nouns: `freelancers`, `jobs`, `comments`
- HTTP methods reflect intent:
  - `POST` create
  - `GET` read
  - `PATCH` partial update
- URLs model relationships:
  - `/freelancers/{freelancerId}/jobs`
  - `/jobs/{jobId}/comments`
- Server owns identifiers and timestamps (`id`, `createdDate`)
- API is stateless: each request includes all info needed to process it

### Planned HTTP Status Codes

- `201 Created` for successful create operations
- `200 OK` for successful reads and updates
- `400 Bad Request` for validation errors
- `404 Not Found` when resource does not exist
- `409 Conflict` for unique constraint conflicts (example: duplicate email)

## Freelancer Endpoints

### Create Freelancer

- `POST /freelancers`
- Request body (draft):
  - `name` (string, required)
  - `email` (string, required)
  - `phone` (string, required)
  - `city` (string, required)
  - `freelancerType` (`DESIGNER` | `SOFTWARE_DEVELOPER`, required)
  - `portfolioUrl` (string, required for designer)
  - `designTools` (array[string], required for designer)
  - `softwareLanguages` (array[string], required for developer)
  - `specialties` (array[string], required for developer)
  - `additionalFields` (map[string,string], optional)
- Behavior:
  - creates freelancer with `evaluationStatus=PENDING`
  - triggers async score calculation

### List Freelancers

- `GET /freelancers`

### Get Freelancer

- `GET /freelancers/{freelancerId}`

### Search Freelancers

- `GET /freelancers/search?name=&city=&type=&tool=&specialty=`

## Job Endpoints

### Create Job

- `POST /jobs`
- Request body (draft):
  - `freelancerId` (long, required)
  - `createdDate` (ISO datetime, optional; server can set)
  - `status` (enum: `IN_PROGRESS`, `FINISHED`, `CANCELLED`, ...)
  - `description` (string, required)

### List Jobs of Freelancer

- `GET /freelancers/{freelancerId}/jobs`

### Get Job

- `GET /jobs/{jobId}`

### Update Job

- `PATCH /jobs/{jobId}`
- Updatable fields:
  - `status`
  - `description`

## Comment Endpoints

### Create Comment for Job

- `POST /jobs/{jobId}/comments`
- Request body (draft):
  - `commenterName` (string, required)
  - `createdDate` (ISO datetime, optional; server can set)
  - `comment` (string, required)

### Read Comments of Job

- `GET /jobs/{jobId}/comments`

### Update Comment

- `PATCH /comments/{commentId}`
- Updatable fields:
  - `comment`

## Response Shape (Draft)

All successful responses will return:

- domain object payload
- metadata where needed (page info, timestamps)

All error responses will return:

- `timestamp`
- `status`
- `error`
- `message`
- `path`
