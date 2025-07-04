package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.service.ExerciseAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.service.ExerciseFactory;

public class ExerciseApplicationService {

    private final ExerciseFactory exerciseFactory;
    private final ExerciseRepository exerciseRepository;

    public ExerciseApplicationService(ExerciseFactory exerciseFactory, ExerciseRepository exerciseRepository) {
        this.exerciseFactory = exerciseFactory;
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise createNewExercise(String name) throws Exception {
        Exercise newExercise = exerciseFactory.of(name);
        return exerciseRepository.create(newExercise);
    }
}
