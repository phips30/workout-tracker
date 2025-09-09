package com.phips30.workouttracker.workout.domain.valueobjects;

import java.util.Objects;

public class RoutineName {

    private final String value;

    public RoutineName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Routine name cannot be null or empty");
        }
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoutineName otherName)) return false;
        return this.value.equals(otherName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
