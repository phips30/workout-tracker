package com.phips30.workouttracker.workout.domain.usecase;

public class RoutineNotFoundException extends Exception {
    public RoutineNotFoundException(String routineName) {
        super(String.format("Routine %s does not exist", routineName));
    }
}
