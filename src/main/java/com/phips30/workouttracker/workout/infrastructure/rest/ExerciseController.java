package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.usecase.ExerciseService;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.ExerciseResponse;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewExerciseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponse>> getExercises() {
        return ResponseEntity.ok(exerciseService.loadAll().stream()
                .map(e -> new ExerciseResponse(e.getId().getId().toString(), e.getName().getValue()))
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Exercise> addExercise(@RequestBody NewExerciseRequest exerciseRequest) {
        try {
            return ResponseEntity.ok(exerciseService.create(exerciseRequest.getName()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
