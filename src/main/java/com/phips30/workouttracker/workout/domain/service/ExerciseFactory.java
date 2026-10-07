package com.phips30.workouttracker.workout.domain.service;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;

/**
 * Creates new exercises and guarantees that exercise names are unique
 */
public class ExerciseFactory {

    private final ExerciseRepository exerciseRepository;

    public ExerciseFactory(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise of(ExerciseName name) throws ExerciseAlreadyExistsException {
        if (exerciseRepository.exists(name)) {
            throw new ExerciseAlreadyExistsException(name.getValue());
        }
        return new Exercise(name);
    }

}
