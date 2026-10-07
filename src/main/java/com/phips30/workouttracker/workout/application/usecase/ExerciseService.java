package com.phips30.workouttracker.workout.application.usecase;

import com.phips30.workouttracker.workout.application.result.ExerciseResult;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.service.ExerciseFactory;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;

import java.util.List;

public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseFactory exerciseFactory;

    public ExerciseService(ExerciseRepository exerciseRepository, ExerciseFactory exerciseFactory) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseFactory = exerciseFactory;
    }

    public ExerciseResult create(String name) throws ExerciseAlreadyExistsException {
        Exercise exercise = exerciseFactory.of(new ExerciseName(name));
        return ExerciseResult.from(exerciseRepository.save(exercise));
    }

    public List<ExerciseResult> loadAll() {
        return exerciseRepository.loadAll().stream()
                .map(ExerciseResult::from)
                .toList();
    }
}
