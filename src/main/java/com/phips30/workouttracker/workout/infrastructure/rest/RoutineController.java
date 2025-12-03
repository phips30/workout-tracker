package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.usecase.*;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.RoutineDetailResponse;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.RoutineRespone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/routine")
public class RoutineController {

    private final RoutineService routineService;

    @Autowired
    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping
    public ResponseEntity<Void> addRoutine(@RequestBody NewRoutineRequest routineRequest) throws RoutineAlreadyExistsException {
        routineService.createRoutine(
                routineRequest.name(),
                routineRequest.routineType(),
                routineRequest.exercises(),
                routineRequest.repetitions()
        );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoutineRespone>> getRoutines() {
        return ResponseEntity.ok(routineService.loadRoutines().stream()
                .map(r -> new RoutineRespone(r.getName().getValue(), r.getRoutineType().toString()))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{name}/detail")
    public ResponseEntity<RoutineDetailResponse> getRoutineDetails(@PathVariable("name") String routineName) throws RoutineNotFoundException {
        Routine r = routineService.loadRoutine(new RoutineName(routineName));
        return ResponseEntity.ok(new RoutineDetailResponse(r.getExercises(), r.getRepetitions()));
    }
}