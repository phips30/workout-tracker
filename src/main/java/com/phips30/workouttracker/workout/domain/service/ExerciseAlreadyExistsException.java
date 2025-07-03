package com.phips30.workouttracker.workout.domain.service;

public class ExerciseAlreadyExistsException extends Exception {
    public ExerciseAlreadyExistsException(String exerciseName) {
        super(String.format("Exercise %s already exists", exerciseName));
    }
}
