package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.domain.usecase.CreateExerciseUseCase;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewExerciseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {

    private final CreateExerciseUseCase createExerciseUseCase;

    @Autowired
    public ExerciseController(CreateExerciseUseCase createExerciseUseCase) {
        this.createExerciseUseCase = createExerciseUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> addExercise(@RequestBody NewExerciseRequest exerciseRequest) {
        try {
            //createExerciseUseCase.execute(new CreateRoutine.InputValues());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
