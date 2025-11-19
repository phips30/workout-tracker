package com.phips30.workouttracker.workout.domain.exceptions;

public class ExerciseAlreadyExistsException extends AlreadyExistsException {
    public ExerciseAlreadyExistsException(String exerciseName) {
        super(String.format("Exercise %s already exists", exerciseName));
    }
}
