package com.phips30.workouttracker.workout.infrastructure.rest;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.usecase.ExerciseApplicationService;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewExerciseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {

    private final ExerciseApplicationService exerciseApplicationService;

    @Autowired
    public ExerciseController(ExerciseApplicationService exerciseApplicationService) {
        this.exerciseApplicationService = exerciseApplicationService;
    }

    @PostMapping
    public ResponseEntity<Void> addExercise(@RequestBody NewExerciseRequest exerciseRequest) {
        try {
            Optional<Exercise> exercise = exerciseApplicationService.createNewExercise(exerciseRequest.getName());
            if(exercise.isPresent()) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
