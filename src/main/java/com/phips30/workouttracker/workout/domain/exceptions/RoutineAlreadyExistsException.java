package com.phips30.workouttracker.workout.domain.exceptions;

import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;

public class RoutineAlreadyExistsException extends AlreadyExistsException {
    public RoutineAlreadyExistsException(RoutineName routineName) {
        super(String.format("Routine %s already exists", routineName.getValue()));
    }
}
