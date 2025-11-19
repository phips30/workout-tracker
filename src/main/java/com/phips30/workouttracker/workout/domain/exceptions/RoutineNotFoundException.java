package com.phips30.workouttracker.workout.domain.exceptions;

import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;

public class RoutineNotFoundException extends NotFoundException {
    public RoutineNotFoundException(RoutineName routineName) {
        super(String.format("Routine %s does not exist", routineName.getValue()));
    }
}
