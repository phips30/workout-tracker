package com.phips30.workouttracker.workout.application.usecase;

import com.phips30.workouttracker.workout.application.result.ExerciseResult;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;

import java.util.List;

public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public ExerciseResult create(String name) throws ExerciseAlreadyExistsException {
        ExerciseName exerciseName = new ExerciseName(name);
        if (exerciseRepository.exists(exerciseName)) {
            throw new ExerciseAlreadyExistsException(name);
        }
        Exercise savedExercise = exerciseRepository.save(new Exercise(exerciseName));
        return ExerciseResult.from(savedExercise);
    }

    public List<ExerciseResult> loadAll() {
        return exerciseRepository.loadAll().stream()
                .map(ExerciseResult::from)
                .toList();
    }
}
