package com.phips30.workouttracker.workout.infrastructure.database;

/**
 * Thrown by the database adapters when data cannot be read from or written to the underlying storage
 */
public class PersistenceException extends RuntimeException {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
