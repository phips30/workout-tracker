package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.application.command.CreateRoutineCommand;
import com.phips30.workouttracker.workout.application.result.RoutineDetailResult;
import com.phips30.workouttracker.workout.application.usecase.RoutineService;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseNotFoundException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.ExerciseResponse;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.RepetitionResponse;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.RoutineDetailResponse;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.RoutineRespone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/routine")
public class RoutineController {

    private final RoutineService routineService;

    @Autowired
    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping
    public ResponseEntity<Void> addRoutine(@RequestBody NewRoutineRequest routineRequest) throws RoutineAlreadyExistsException, ExerciseNotFoundException {
        routineService.createRoutine(new CreateRoutineCommand(
                routineRequest.name(),
                routineRequest.routineType(),
                routineRequest.exerciseIds().stream().map(UUID::fromString).toList(),
                routineRequest.repetitions()
        ));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoutineRespone>> getRoutines() {
        return ResponseEntity.ok(routineService.loadRoutines().stream()
                .map(r -> new RoutineRespone(r.name(), r.routineType()))
                .toList());
    }

    @GetMapping("/{name}/detail")
    public ResponseEntity<RoutineDetailResponse> getRoutineDetails(@PathVariable("name") String routineName) throws RoutineNotFoundException {
        RoutineDetailResult routine = routineService.loadRoutine(routineName);
        return ResponseEntity.ok(new RoutineDetailResponse(
                routine.exercises().stream()
                        .map(e -> new ExerciseResponse(e.id(), e.name()))
                        .toList(),
                routine.repetitions().stream()
                        .map(r -> new RepetitionResponse(r.type(), r.number()))
                        .toList()));
    }
}
