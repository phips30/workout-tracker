package com.phips30.workouttracker.workout.rest;

import com.phips30.workouttracker.workout.domain.usecase.CreateRoutine;
import com.phips30.workouttracker.workout.rest.dto.NewRoutineRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/workouts")
public class RoutineController {

    private final CreateRoutine createRoutineUseCase;

    @Autowired
    public RoutineController(CreateRoutine createRoutineUseCase) {
        this.createRoutineUseCase = createRoutineUseCase;
    }

    @PostMapping("{id}")
    public ResponseEntity<Void> createRoutine(@RequestBody NewRoutineRequest routineRequest) {
        try {
            createRoutineUseCase.execute(new CreateRoutine.InputValues());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}