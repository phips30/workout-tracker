package com.phips30.workouttracker.workout.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    @PostMapping("{id}")
    public ResponseEntity<Void> addNewWorkout(@PathVariable UUID id, @RequestBody Object workout) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}