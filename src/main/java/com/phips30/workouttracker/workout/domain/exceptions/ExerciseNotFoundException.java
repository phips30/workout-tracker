package com.phips30.workouttracker.workout.domain.exceptions;

import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;

public class ExerciseNotFoundException extends NotFoundException {
    public ExerciseNotFoundException(EntityId exerciseId) {
        super(String.format("Exercise %s does not exist", exerciseId));
    }
}
