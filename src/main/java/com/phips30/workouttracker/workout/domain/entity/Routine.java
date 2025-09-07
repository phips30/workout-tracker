package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Aggregate root routine
 */
public class Routine {
    private final EntityId id;
    private final String name;
    private final RoutineType routineType;
    private final List<Exercise> exercises;
    private final List<Repetition> repetitions;
    private final ZonedDateTime createdAt;

    private Routine(EntityId id, String name, RoutineType routineType, List<Exercise> exercises, List<Repetition> repetitions, ZonedDateTime createdAt) {
        if (id == null) {
            throw new IllegalArgumentException("Entity id is null");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Routine name is null or empty");
        }
        if (routineType == null) {
            throw new IllegalArgumentException("RoutineType is null");
        }
        if (exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("Exercises is null or empty");
        }
        if (repetitions == null || repetitions.isEmpty()) {
            throw new IllegalArgumentException("Repetitions is null or empty");
        }
        if(exercises.size() != repetitions.size()) {
            throw new IllegalArgumentException("Each exercise must have a corresponding repetition");
        }

        this.id = id;
        this.name = name;
        this.routineType = routineType;
        this.exercises = exercises;
        this.repetitions = repetitions;
        this.createdAt = createdAt;
    }

    public static Routine createNew(
            String name,
            RoutineType routineType,
            List<Exercise> exercises,
            List<Repetition> repetitions) {
        return new Routine(EntityId.generate(), name, routineType, exercises, repetitions, ZonedDateTime.now());
    }

    public static Routine of(
            EntityId id,
            String name,
            RoutineType routineType,
            List<Exercise> exercises,
            List<Repetition> repetitions,
            ZonedDateTime createdAt) {
        return new Routine(id, name, routineType, exercises, repetitions, createdAt);
    }

    public List<Exercise> getExercises() {
        return Collections.unmodifiableList(this.exercises);
    }

    public List<Repetition> getRepetitions() {
        return Collections.unmodifiableList(this.repetitions);
    }

    public String getName() {
        return this.name;
    }

    public RoutineType getRoutineType() {
        return routineType;
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean equalObjects = false;

        if (otherObject != null && this.getClass() == otherObject.getClass()) {
            Routine routine = (Routine) otherObject;
            equalObjects = this.id.equals(routine.id);
        }
        return equalObjects;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return "Routine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", routineType=" + routineType +
                ", createdAt=" + createdAt +
                '}';
    }
}
