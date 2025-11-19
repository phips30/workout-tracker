package com.phips30.workouttracker.workout.domain.exceptions;

public abstract class NotFoundException extends Throwable {
    public NotFoundException(String message) {
        super(message);
    }
}
