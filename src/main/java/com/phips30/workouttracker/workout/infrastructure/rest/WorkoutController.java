package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.usecase.WorkoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout")
public class WorkoutController {

    private WorkoutService workoutService;

    @PostMapping("{routineName}")
    public ResponseEntity<Void> addNewWorkout(@PathVariable String routineName, @RequestBody Workout workout) {
        workoutService.saveWorkout(routineName, workout);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{routineName}")
    public ResponseEntity<List<Workout>> getWorkouts(@PathVariable String routineName) {
        return ResponseEntity.ok(workoutService.loadWorkoutsForRoutine(routineName));
    }
}