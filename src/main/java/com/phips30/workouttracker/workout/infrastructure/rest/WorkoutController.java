package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.usecase.WorkoutService;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewWorkoutRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/routines/{routineName}/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<Void> addNewWorkout(@PathVariable String routineName, @RequestBody NewWorkoutRequest workout)
            throws RoutineNotFoundException {
        Workout workoutToSave = Workout.of(
                workout.startedAt(),
                workout.rounds(),
                workout.metadata()
        );

        workoutService.saveWorkout(routineName, workoutToSave);
        return ResponseEntity
                .created(URI.create(String.format("/api/routines/%s/workouts", routineName)))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<Workout>> getWorkouts(@PathVariable String routineName) {
        List<Workout> workouts = workoutService.loadWorkoutsForRoutine(routineName);

        if (workouts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(workouts);
    }
}