package com.phips30.workouttracker.workout.domain.service;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;

public class ExerciseFactory {

    private final ExerciseRepository exerciseRepository;

    public ExerciseFactory(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise of(String name) throws ExerciseAlreadyExistsException {
        if (exerciseRepository.exists(name)) {
            throw new ExerciseAlreadyExistsException(name);
        }
        return Exercise.of(name);
    }

}
