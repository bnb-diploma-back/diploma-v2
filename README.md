# SDU Academic Planner

AI-powered academic planning platform for university students. Built as a diploma project at Suleyman Demirel University.

## What it does

- **Student & Syllabus Management** — full CRUD for student profiles and course syllabi with weekly plans
- **Weekly Task Tracking** — tasks grouped by course and week, with target grade awareness
- **AI Weekly Organizer** — GPT-4o generates balanced daily study schedules with effort levels, focus tips, and well-being advice
- **AI Career Cards** — analyzes courses and grades to generate ranked career recommendations with match percentages and gap analysis
- **Dashboard** — single endpoint aggregating profile, academics, deadlines, progress, careers, and today's schedule
- **AI Chat** — real-time personalized academic assistant via WebSocket (STOMP over SockJS)

## Tech Stack

- Java 25, Spring Boot 4.0.3, Spring Cloud (Eureka + Config Server)
- Spring Data JPA (writes) + jOOQ 3.20 (reads) — CQRS-like split
- PostgreSQL, Lombok, SpringDoc OpenAPI 2.8.6
- OpenAI GPT-4o (via `com.openai:openai-java`)
- WebSocket with STOMP + SockJS
- Micrometer + Prometheus metrics

## Architecture

Three Gradle modules with vertical feature slicing:

```
launcher/          — Spring Boot entry point, configs (OpenAPI, Metrics, Validator)
api/               — REST controllers, business logic, AI services
library/           — JPA entities, CrudRepositories, jOOQ generated classes, test infrastructure
```

Each domain follows the delegate pattern:

```
api/{domain}/
  {Domain}Controller.java          — thin REST layer with Swagger annotations
  {Domain}Delegate.java            — routes to per-operation APIs
  create/Create{Domain}Api.java    — @Transactional, uses JPA
  get/Get{Domain}Api.java          — @Transactional(readOnly=true)
  get/Get{Domain}Repository.java   — uses jOOQ DSLContext
  update/Update{Domain}Api.java    — @Transactional, uses JPA
  delete/Delete{Domain}Api.java    — @Transactional, uses JPA
```

## Prerequisites

Docker containers from `docker/docker-compose.yml`:

| Service       | Port |
|---------------|------|
| PostgreSQL    | 5432 |
| Test DB       | 5433 |
| Eureka        | 8761 |
| Config Server | 8888 |

```bash
cd docker && docker-compose up -d
```

Environment variable for AI features:

```bash
export OPENAI_API_KEY=sk-your-key-here
```

## Build & Run

```bash
./gradlew clean build        # Full build
./gradlew bootRun            # Run app (port 8070)
./gradlew test               # All tests
./gradlew api:test           # API module tests only
```

## API Endpoints

Swagger UI available at: `http://localhost:8070/swagger-ui.html`

### Students `/api/v1/students`

| Method   | Path                          | Description                     |
|----------|-------------------------------|---------------------------------|
| `GET`    | `/`                           | List all students               |
| `GET`    | `/{id}`                       | Get student by ID               |
| `GET`    | `/by-student-id/{studentId}`  | Get student by university ID    |
| `POST`   | `/`                           | Create student                  |
| `PUT`    | `/{id}`                       | Update student                  |
| `DELETE` | `/{id}`                       | Delete student                  |

### Syllabi `/api/v1/syllabi`

| Method   | Path                              | Description                  |
|----------|-----------------------------------|------------------------------|
| `GET`    | `/`                               | List all syllabi             |
| `GET`    | `/{id}`                           | Get syllabus by ID           |
| `GET`    | `/by-course-code/{courseCode}`    | Get syllabus by course code  |
| `POST`   | `/`                               | Create syllabus              |
| `PUT`    | `/{id}`                           | Update syllabus              |
| `DELETE` | `/{id}`                           | Delete syllabus              |

### Weekly Planner `/api/v1/weekly`

| Method | Path                                              | Description                          |
|--------|----------------------------------------------------|--------------------------------------|
| `GET`  | `/students/{id}/weeks/{week}`                      | Get tasks grouped by course          |
| `POST` | `/students/{id}/weeks/{week}/organize`             | Generate AI study organizer          |
| `GET`  | `/students/{id}/weeks/{week}/organize`             | Get saved AI organizer               |

### Career Cards `/api/v1/careers`

| Method | Path                              | Description                      |
|--------|-----------------------------------|----------------------------------|
| `POST` | `/students/{id}/generate`         | Generate AI career cards         |
| `GET`  | `/students/{id}`                  | Get saved career cards           |

### Dashboard `/api/v1/dashboard`

| Method | Path                       | Description                                |
|--------|----------------------------|--------------------------------------------|
| `GET`  | `/students/{id}?week=3`   | Aggregated dashboard (profile, tasks, etc.) |

### WebSocket Chat

| Protocol | Endpoint       | Description                        |
|----------|----------------|------------------------------------|
| SockJS   | `/ws/chat`     | WebSocket connection endpoint      |
| STOMP    | `/app/chat`    | Send message (destination)         |
| STOMP    | `/user/queue/chat` | Subscribe for streamed responses |

**Message format:**

```json
// Send to /app/chat
{ "studentId": 1, "message": "How should I prepare for my CS midterm?" }

// Receive from /user/queue/chat
{ "content": "Based on your", "type": "CHUNK", "timestamp": "..." }
{ "content": "", "type": "DONE", "timestamp": "..." }
```

## Database Schema

```
students            — student profiles
syllabi             — course syllabi
weekly_plans        — weekly teaching plans per syllabus
student_syllabi     — student-course enrollments with expected grades
student_careers     — AI-generated career cards per student
student_tasks       — homework/lab/project tasks per student, course, week
weekly_organizers   — saved AI-generated weekly study plans
```

## Testing

Tests use a real PostgreSQL test database (port 5433). Test infrastructure in `library/src/main/java/.../test/`:

- **`BaseTest`** — `@SpringBootTest` base class
- **`Creator`** — factory methods for test entities
- **`Remover`** — cleanup with FK-safe ordering
- **`Randomizer`** — random test values
- **`builder/`** — fluent builders with randomized defaults

```bash
./gradlew api:test                                    # All API tests
./gradlew test --tests '*GetDashboardApiTest'         # Single test class
./gradlew test --tests '*GetWeeklyApiTest.findBy*'    # Single test method
```

## Migration

SQL migration script: `library/src/main/resources/student_task_migration.sql`

Contains `CREATE TABLE` statements for `student_tasks` and `weekly_organizers`, plus `ALTER TABLE` for adding `estimated_hours` to existing databases.