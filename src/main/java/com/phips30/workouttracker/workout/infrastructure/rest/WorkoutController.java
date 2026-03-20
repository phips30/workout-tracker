package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.usecase.WorkoutService;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewWorkoutRequest;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.WorkoutResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
        Workout workoutToSave = Workout.of(
                workout.startedAt(),
                workout.rounds(),
                workout.metadata()
        );

        workoutService.saveWorkout(routineName, workoutToSave);
        return ResponseEntity
                .created(URI.create(String.format("/api/routine/%s/workout", routineName)))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<WorkoutResponse>> getWorkoutsForRoutine(@PathVariable String routineName) {
        List<Workout> workouts = workoutService.loadWorkoutsForRoutine(routineName);
        return ResponseEntity.ok(workouts.stream()
                .map(w -> new WorkoutResponse(
                        w.getId().getId().toString(),
                        w.getStartedAt(),
                        w.getRounds().stream().map(r -> r.getDuration().toMillis()).toList(),
                        w.getMetadata()))
                .toList());
    }
}