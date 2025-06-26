package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.usecase.CreateRoutine;
import com.phips30.workouttracker.workout.domain.usecase.LoadRoutine;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/routine")
public class RoutineController {

    private final CreateRoutine createRoutineUseCase;
    private final LoadRoutine loadRoutineUseCase;

    @Autowired
    public RoutineController(CreateRoutine createRoutineUseCase, LoadRoutine loadRoutineUseCase) {
        this.createRoutineUseCase = createRoutineUseCase;
        this.loadRoutineUseCase = loadRoutineUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> addRoutine(@RequestBody NewRoutineRequest routineRequest) {
        try {
            createRoutineUseCase.execute(new CreateRoutine.InputValues());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{name}")
    @ResponseBody
    public ResponseEntity<Routine> getRoutine(@PathVariable("name") String routineName) {
        try {
            return ResponseEntity.ok(loadRoutineUseCase.loadRoutineWithWorkouts(routineName));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}