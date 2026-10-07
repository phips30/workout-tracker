package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.application.command.CreateWorkoutSessionCommand;
import com.phips30.workouttracker.workout.application.result.WorkoutSessionResult;
import com.phips30.workouttracker.workout.application.usecase.WorkoutSessionService;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewWorkoutSessionRequest;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.WorkoutSessionEntryResponse;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.WorkoutSessionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.phips30.workouttracker.workout.infrastructure.rest.WorkoutSessionController.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
public class WorkoutSessionController {

    protected static final String BASE_PATH = "/api/routine/{routineName}/workout-session";

    private final WorkoutSessionService workoutSessionService;

    public WorkoutSessionController(WorkoutSessionService workoutSessionService) {
        this.workoutSessionService = workoutSessionService;
    }

    @PostMapping
    public ResponseEntity<Void> createWorkoutSession(@PathVariable String routineName,
                                                     @RequestBody NewWorkoutSessionRequest workoutSession)
            throws RoutineNotFoundException {
        workoutSessionService.saveWorkoutSession(new CreateWorkoutSessionCommand(
                routineName,
                workoutSession.startedAt(),
                workoutSession.metadata(),
                workoutSession.entries() == null
                        ? List.of()
                        : workoutSession.entries().stream()
                        .map(e -> new CreateWorkoutSessionCommand.Entry(
                                e.blockPosition(),
                                e.round(),
                                e.itemPosition(),
                                e.completedRepetitions() == null ? 0 : e.completedRepetitions(),
                                Boolean.TRUE.equals(e.completed())))
                        .toList()
        ));
        return ResponseEntity
                .created(URI.create(String.format("/api/routine/%s/workout-session", routineName)))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<WorkoutSessionResponse>> getWorkoutSessionsForRoutine(@PathVariable String routineName)
            throws RoutineNotFoundException {
        List<WorkoutSessionResult> sessions = workoutSessionService.loadWorkoutSessionsForRoutine(routineName);
        return ResponseEntity.ok(sessions.stream()
                .map(s -> new WorkoutSessionResponse(
                        s.id(),
                        s.startedAt(),
                        s.metadata(),
                        s.entries().stream()
                                .map(e -> new WorkoutSessionEntryResponse(
                                        e.blockPosition(),
                                        e.round(),
                                        e.itemPosition(),
                                        e.repetitionType(),
                                        e.completedRepetitions(),
                                        e.completed()))
                                .toList()))
                .toList());
    }
}
