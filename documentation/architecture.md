# Architecture

The backend follows an onion architecture: dependencies only point inward.

```
infrastructure  ->  application  ->  domain
```

Base package: `com.phips30.workouttracker.workout`

## Layers

### domain
Business model and rules. No dependency on Spring, Jackson or any other layer.

- `entity`: aggregates and entities (`Routine`, `Workout`, `Exercise`)
- `valueobjects`: immutable, self-validating types (`EntityId`, `RoutineName`, `ExerciseName`, `Repetition`, `Round`)
- `repository`: repository interfaces (ports) implemented by infrastructure
- `service`: domain services (`ExerciseFactory`)
- `exceptions`: domain exceptions
- `util`: assertion helpers

### application
Use cases that orchestrate the domain. Plain Java, no Spring annotations. May depend on `domain` only.

- `usecase`: `ExerciseService`, `RoutineService`, `WorkoutService`
- `command`: input objects for use cases (`CreateRoutineCommand`, `CreateWorkoutCommand`)
- `result`: output objects returned by use cases (`ExerciseResult`, `RoutineResult`, `RoutineDetailResult`, `WorkoutResult`, ...). They contain only primitives, strings, time types and other results, and are created from domain objects via `from(...)` factory methods.

### infrastructure
Adapters and framework code. May depend on `application` and `domain`.

- `rest`: Spring controllers, request/response DTOs, exception handler (kept inside infrastructure, no separate presentation layer). Controllers map DTOs to commands and results to DTOs.
- `database/json`: JSON file implementations of the repository ports, DB entities and mappers
- `configuration`: Spring wiring of the use cases (`UseCaseSetup`)

## Rules

- `domain` must not import from `application` or `infrastructure`.
- `application` must not import from `infrastructure`.
- Domain entities and value objects never leave the application layer: use cases accept commands/primitives and return results. Controllers and DTOs must not import `domain.entity` or `domain.valueobjects` (domain exceptions are the only domain types used outside).
- Use cases are registered as beans in `infrastructure/configuration/UseCaseSetup`, not annotated themselves.
- Repository ports (`domain/repository`) use domain types (`EntityId`, `ExerciseName`, `RoutineName`, entities) in their signatures, never raw `String`/`UUID`. Adapters convert to and from their storage types.
- Adapters depend on ports, not on other adapters: e.g. `RoutineRepositoryImpl` is injected with `ExerciseRepository`, not `ExerciseRepositoryImpl`.
- Persistence models (`*DbEntity`) stay inside the database adapter and are mapped to domain objects.

## Tests

Tests mirror the main package structure (`domain`, `application`, `infrastructure`).
