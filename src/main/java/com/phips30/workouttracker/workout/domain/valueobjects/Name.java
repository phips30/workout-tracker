package com.phips30.workouttracker.workout.domain.valueobjects;

import java.util.Objects;

public abstract class Name {

    private final String value;

    public Name(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(String.format("%s cannot be null or empty", this.getClass().getSimpleName()));
        }
        if (value.trim().length() <= 2) {
            throw new IllegalArgumentException(String.format("%s cannot be less than three characters", this.getClass().getSimpleName()));
        }
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Name otherName)) return false;
        return this.value.equals(otherName.getValue());
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
