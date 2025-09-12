package com.phips30.workouttracker.workout.domain.util;

import java.util.Collection;

public class AssertionHelper {
    public static void assertNotNull(Object object, String message) {
        if(object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void assertNotNullOrEmpty(Collection<T> object, String message) {
        if(object == null || object.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}
