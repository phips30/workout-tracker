package com.phips30.workouttracker.workout.domain.exceptions;

public abstract class AlreadyExistsException extends Throwable {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
