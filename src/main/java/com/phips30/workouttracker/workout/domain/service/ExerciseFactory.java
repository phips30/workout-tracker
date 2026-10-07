package com.phips30.workouttracker.workout.domain.service;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;

public class ExerciseFactory {

    private final ExerciseRepository exerciseRepository;

    public ExerciseFactory(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise of(String name) throws ExerciseAlreadyExistsException {
        ExerciseName exerciseName = new ExerciseName(name);
        if (exerciseRepository.exists(exerciseName)) {
            throw new ExerciseAlreadyExistsException(name);
        }
        return new Exercise(exerciseName);
    }

}
