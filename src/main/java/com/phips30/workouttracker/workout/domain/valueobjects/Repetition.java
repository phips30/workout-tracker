package com.phips30.workouttracker.workout.domain.valueobjects;

import com.phips30.workouttracker.workout.domain.util.AssertionHelper;

import java.util.Objects;

public final class Repetition {
    private final RepetitionType type;
    private final int number;

    private Repetition(RepetitionType type, int number) {
        AssertionHelper.assertNotNull(type, "RepetitionType is null");
        if (number <= 0) {
            throw new IllegalArgumentException("Number must be greater or equal to 1");
        }
        this.type = type;
        this.number = number;
    }

    public static Repetition of(int number) {
        return new Repetition(RepetitionType.NUMBER, number);
    }

    public static Repetition of(RepetitionType type, int number) {
        return new Repetition(type, number);
    }

    public RepetitionType getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repetition that = (Repetition) o;
        return number == that.number && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, number);
    }

    @Override
    public String toString() {
        return "Repetition{" +
                "type=" + type +
                ", number=" + number +
                '}';
    }
}
