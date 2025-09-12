package com.phips30.workouttracker.workout.domain.util;

public class AssertionHelper {
    public static void assertNotNull(Object object, String message) {
        if(object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
