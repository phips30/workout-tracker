package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.application.result.ExerciseResult;
import com.phips30.workouttracker.workout.application.usecase.ExerciseService;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseAlreadyExistsException;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.ExerciseResponse;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewExerciseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.phips30.workouttracker.workout.infrastructure.rest.ExerciseController.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
public class ExerciseController {

    protected static final String BASE_PATH = "/api/exercise";

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponse>> getExercises() {
        return ResponseEntity.ok(exerciseService.loadAll().stream()
                .map(this::toResponse)
                .toList());
    }

    @PostMapping
    public ResponseEntity<ExerciseResponse> addExercise(@RequestBody NewExerciseRequest exerciseRequest) throws ExerciseAlreadyExistsException {
        ExerciseResult exercise = exerciseService.create(exerciseRequest.name());
        return ResponseEntity.created(URI.create(BASE_PATH))
                .body(toResponse(exercise));
    }

    private ExerciseResponse toResponse(ExerciseResult exercise) {
        return new ExerciseResponse(exercise.id(), exercise.name());
    }
}
