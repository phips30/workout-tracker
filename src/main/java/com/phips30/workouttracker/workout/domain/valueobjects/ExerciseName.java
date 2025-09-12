package com.phips30.workouttracker.workout.domain.valueobjects;

import java.util.Objects;

public class ExerciseName {

    private final String value;

    public ExerciseName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Exercise name cannot be null or empty");
        }
        if (value.trim().length() <= 2) {
            throw new IllegalArgumentException("Exercise name cannot be less than three characters");
        }
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseName otherName)) return false;
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
