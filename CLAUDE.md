# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
./gradlew clean build              # Full build
./gradlew bootRun                  # Run app (port 8070, requires config-server + eureka + postgres)
./gradlew test                     # All tests
./gradlew api:test                 # API module tests only
./gradlew test --tests '*CreateSyllabusApiTest'  # Single test class
```

**Prerequisites**: Docker containers from `docker/docker-compose.yml` must be running (postgres on 5432, test_db on 5433, eureka on 8761, config-server on 8888).

**jOOQ codegen**: Runs automatically during `compileJava` in the `library` module. Requires the main postgres database to be running. Generated sources go to `library/build/generated-sources/jooq/`.

## Architecture

**Three Gradle modules**:
- **`launcher`** — Spring Boot entry point, configs (OpenAPI, Metrics, Validator)
- **`api`** — REST controllers, business logic, organized by domain
- **`library`** — JPA entities, CrudRepositories, jOOQ generated classes, shared test infrastructure

**Vertical feature slicing with delegate pattern** — each domain is organized as:
```
api/{domain}/
├── {Domain}Controller.java          # Thin REST layer
├── {Domain}Delegate.java            # Routes to per-operation APIs
├── create/Create{Domain}Api.java    # @Service, @Transactional, uses JPA
├── get/Get{Domain}Api.java          # @Service, @Transactional(readOnly=true)
├── get/Get{Domain}Repository.java   # @Repository, uses jOOQ DSLContext
├── update/Update{Domain}Api.java    # @Service, @Transactional, uses JPA
└── delete/Delete{Domain}Api.java    # @Service, @Transactional, uses JPA
```

**CQRS-like read/write split**: Writes go through JPA CrudRepository (in `library`). Reads use jOOQ DSLContext with type-safe queries (repositories in `api` per-domain `get/` packages).

## Code Style

- `final var` for local variables; no `final` on method parameters
- Constructor injection via Lombok `@RequiredArgsConstructor`
- Entities: `@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder`
- API naming: `{Verb}{Domain}Api`, requests: `{Verb}{Domain}Request`, responses: `Get{Domain}Response`
- Endpoints: `/api/v1/{plural-domain}`

## Testing

Test infrastructure lives in `library/src/main/java/.../test/` (shared across modules):
- **`BaseTest`** — `@SpringBootTest` base class, all tests extend this
- **`Creator`** / **`Editor`** / **`Remover`** — create, modify, and clean test data
- **`Randomizer`** — generates random test values (str, name, text, code, id, date, semester)
- **`builder/`** — fluent builders with randomizer defaults (e.g., `SyllabusTest`)

Test pattern:
```java
class MyTest extends BaseTest {
    @BeforeEach
    void setUp() { remover.all(); }

    @Test
    void method_expectedBehavior() {
        final var entity = creator.syllabus();
        // AssertJ assertions
    }
}
```

Test database: PostgreSQL on port 5433 (docker container `test_db`), configured in `api/src/test/resources/application.yml`.

## Tech Stack

Spring Boot 4.0.3, Spring Cloud (Eureka + Config Server), Spring Data JPA, jOOQ 3.20.11, PostgreSQL, Lombok, SpringDoc OpenAPI 2.8.6, Micrometer/Prometheus, Java 25.