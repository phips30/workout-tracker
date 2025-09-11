package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;

public class RoutineAlreadyExistsException extends Exception {
    public RoutineAlreadyExistsException(RoutineName routineName) {
        super(String.format("Routine %s already exists", routineName.getValue()));
    }
}
