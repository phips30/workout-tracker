package com.phips30.workouttracker.workout.domain.util;

import java.util.Collection;
import java.util.List;

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

    /**
     * Asserts that the positions are unique and form the sequence 1..n (in any order)
     */
    public static void assertConsecutivePositions(List<Integer> positions, String message) {
        List<Integer> sorted = positions.stream().sorted().toList();
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i) != i + 1) {
                throw new IllegalArgumentException(message);
            }
        }
    }
}
