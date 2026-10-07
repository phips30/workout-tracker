package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.util.AssertionHelper;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;

import java.util.Objects;

/**
 * Part of a {@link RoutineBlock}: an exercise with the repetitions (number or seconds) to perform
 * at a given position within the block. Has no identity of its own.
 */
public final class RoutineBlockItem {
    private final int position;
    private final Exercise exercise;
    private final Repetition repetition;

    private RoutineBlockItem(int position, Exercise exercise, Repetition repetition) {
        if (position < 1) {
            throw new IllegalArgumentException("Position must be greater or equal to 1");
        }
        AssertionHelper.assertNotNull(exercise, "Exercise is null");
        AssertionHelper.assertNotNull(repetition, "Repetition is null");

        this.position = position;
        this.exercise = exercise;
        this.repetition = repetition;
    }

    public static RoutineBlockItem of(int position, Exercise exercise, Repetition repetition) {
        return new RoutineBlockItem(position, exercise, repetition);
    }

    public int getPosition() {
        return position;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public Repetition getRepetition() {
        return repetition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutineBlockItem that = (RoutineBlockItem) o;
        return position == that.position && exercise.equals(that.exercise) && repetition.equals(that.repetition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, exercise, repetition);
    }

    @Override
    public String toString() {
        return "RoutineBlockItem{" +
                "position=" + position +
                ", exercise=" + exercise +
                ", repetition=" + repetition +
                '}';
    }
}
