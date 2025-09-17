package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.service.ExerciseFactory;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;

public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise create(String name) throws ExerciseAlreadyExistsException {
        if (exerciseRepository.exists(name)) {
            throw new ExerciseAlreadyExistsException(name);
        }
        return new Exercise(new ExerciseName(name));
    }
}
