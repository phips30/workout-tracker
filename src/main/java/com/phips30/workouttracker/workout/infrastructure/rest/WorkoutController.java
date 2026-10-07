package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.application.command.CreateWorkoutCommand;
import com.phips30.workouttracker.workout.application.result.WorkoutResult;
import com.phips30.workouttracker.workout.application.usecase.WorkoutService;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewWorkoutRequest;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.RoundRequest;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.WorkoutResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;
import java.util.List;

import static com.phips30.workouttracker.workout.infrastructure.rest.WorkoutController.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
public class WorkoutController {

    protected static final String BASE_PATH = "/api/routine/{routineName}/workout";

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<Void> createWorkout(@PathVariable String routineName, @RequestBody NewWorkoutRequest workout)
            throws RoutineNotFoundException {
        workoutService.saveWorkout(new CreateWorkoutCommand(
                routineName,
                workout.startedAt(),
                workout.rounds() == null ? List.of() : workout.rounds().stream().map(RoundRequest::duration).toList(),
                workout.metadata()
        ));
        return ResponseEntity
                .created(URI.create(String.format("/api/routine/%s/workout", routineName)))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<WorkoutResponse>> getWorkoutsForRoutine(@PathVariable String routineName) {
        List<WorkoutResult> workouts = workoutService.loadWorkoutsForRoutine(routineName);
        return ResponseEntity.ok(workouts.stream()
                .map(w -> new WorkoutResponse(
                        w.id(),
                        w.startedAt(),
                        w.roundDurations().stream().map(Duration::toMillis).toList(),
                        w.metadata()))
                .toList());
    }
}
