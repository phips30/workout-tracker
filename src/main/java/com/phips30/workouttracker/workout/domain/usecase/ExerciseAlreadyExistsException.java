package com.phips30.workouttracker.workout.domain.usecase;

public class ExerciseAlreadyExistsException extends Exception {
    public ExerciseAlreadyExistsException(String exerciseName) {
        super(String.format("Exercise %s already exists", exerciseName));
    }
}
