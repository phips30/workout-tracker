package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.usecase.CreateRoutine;
import com.phips30.workouttracker.workout.domain.usecase.LoadRoutine;
import com.phips30.workouttracker.workout.domain.usecase.RoutineAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.usecase.RoutineNotFoundException;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<Void> addRoutine(@RequestBody NewRoutineRequest routineRequest) throws RoutineAlreadyExistsException {
        createRoutineUseCase.execute(
                routineRequest.name(),
                routineRequest.routineType(),
                routineRequest.exercises(),
                routineRequest.repetitions()
        );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Routine> getRoutine(@PathVariable("name") String routineName) throws RoutineNotFoundException {
        return ResponseEntity.ok(loadRoutineUseCase.loadRoutine(routineName));
    }
}