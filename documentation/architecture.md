# Architecture

The backend follows an onion architecture: dependencies only point inward.

```
infrastructure  ->  application  ->  domain
```

Base package: `com.phips30.workouttracker.workout`

## Layers

### domain
Business model and rules. No dependency on Spring, Jackson or any other layer.

- `entity`: aggregates and entities (`Routine`, `WorkoutSession`, `Exercise`). Aggregates are created with `createNew(...)` (generates an id) or reconstituted from storage with `of(id, ...)`, and never expose mutable collections. A `WorkoutSession` references its `Routine` by `routineId`.
  - A `Routine` consists of ordered `RoutineBlock`s (position, rounds), each consisting of ordered `RoutineBlockItem`s (position, `Exercise`, `Repetition` as number or seconds). Blocks and items have no identity of their own, are immutable and part of the `Routine` aggregate. Positions start at 1 and must be unique and gapless within their parent.
  - A `WorkoutSession` is one execution of a routine: a start time, free-form `metadata` (e.g. the added weight) and `WorkoutSessionEntry`s. An entry (immutable, part of the aggregate) references a routine block item by block position, item position and round, and holds the completed repetitions for `NUMBER` items or whether the item was completed for `SECONDS` items. `WorkoutSession.createNew(routine, ...)` validates that every entry references an existing item and round of the routine and appears only once.
- `valueobjects`: immutable, self-validating types (`EntityId`, `RoutineName`, `ExerciseName`, `Repetition`, `RepetitionType`). Value objects are final, immutable and implement `equals`/`hashCode`.
- `repository`: repository interfaces (ports) implemented by infrastructure
- `service`: domain services that enforce business rules needing repository access: `ExerciseFactory` (exercise names are unique) and `RoutineFactory` (routine names are unique, every referenced exercise exists)
- `exceptions`: domain exceptions
- `util`: assertion helpers

### application
Use cases that orchestrate the domain. Plain Java, no Spring annotations. May depend on `domain` only.

- `usecase`: `ExerciseService`, `RoutineService`, `WorkoutSessionService`
- `command`: input objects for use cases (`CreateRoutineCommand`, `CreateWorkoutSessionCommand`)
- `result`: output objects returned by use cases (`ExerciseResult`, `RoutineResult`, `RoutineDetailResult`, `RoutineBlockResult`, `WorkoutSessionResult`, ...). They contain only primitives, strings, time types and other results, and are created from domain objects via `from(...)` factory methods.

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
- Business rules live in the domain (entities, value objects, domain services), not in use cases or adapters. Use cases only orchestrate (call the domain, then persist); adapters only store and load data and must not skip or reject saves on their own.
- Repository ports (`domain/repository`) use domain types (`EntityId`, `ExerciseName`, `RoutineName`, entities) in their signatures, never raw `String`/`UUID`. Adapters convert to and from their storage types.
- Adapters depend on ports, not on other adapters: e.g. `RoutineRepositoryImpl` is injected with `ExerciseRepository`, not `ExerciseRepositoryImpl`.
- Adapters never hide storage failures: if data cannot be read or written they throw `infrastructure.database.PersistenceException` (unchecked, with the original exception as cause) instead of logging and returning an empty result or silently succeeding. The REST exception handler turns it into a 500 response.
- Persistence models (`*DbEntity`) stay inside the database adapter and are mapped to domain objects.

## Tests

Tests mirror the main package structure (`domain`, `application`, `infrastructure`).
