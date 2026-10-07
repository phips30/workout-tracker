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

### infrastructure
Adapters and framework code. May depend on `application` and `domain`.

- `rest`: Spring controllers, DTOs, exception handler (kept inside infrastructure, no separate presentation layer)
- `database/json`: JSON file implementations of the repository ports, DB entities and mappers
- `configuration`: Spring wiring of the use cases (`UseCaseSetup`)

## Rules

- `domain` must not import from `application` or `infrastructure`.
- `application` must not import from `infrastructure`.
- Use cases are registered as beans in `infrastructure/configuration/UseCaseSetup`, not annotated themselves.
- Persistence models (`*DbEntity`) stay inside the database adapter and are mapped to domain objects.

## Tests

Tests mirror the main package structure (`domain`, `application`, `infrastructure`).
