package com.phips30.workouttracker.workout.domain.usecase;

public class RoutineAlreadyExistsException extends Exception {
    public RoutineAlreadyExistsException(String routineName) {
        super(String.format("Routine %s already exists", routineName));
    }
}
