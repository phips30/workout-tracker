package com.phips30.workouttracker.workout.domain.exceptions;

public abstract class AlreadyExistsException extends Exception {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
