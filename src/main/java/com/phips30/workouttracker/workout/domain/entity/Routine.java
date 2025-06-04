package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.Exercise;

import java.util.ArrayList;
import java.util.List;

public class Routine {
    private String name;
    private List<Exercise> exercises;
    private List<Integer> repetitions;
    private List<Workout> workouts;

    private Routine(String name, List<Exercise> exercises, List<Integer> repetitions) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Routine name is null or empty");
        }
        if (exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("Exercises is null or empty");
        }
        if (repetitions == null || repetitions.isEmpty()) {
            throw new IllegalArgumentException("Repetitions is null or empty");
        }

        this.name = name;
        this.exercises = exercises;
        this.repetitions = repetitions;
        this.workouts = new ArrayList<>();
    }

    private Routine(String name, List<Exercise> exercises, List<Integer> repetitions, List<Workout> workouts) {
        this(name, exercises, repetitions);
        this.workouts = workouts;
    }

    public static Routine of(
            String name,
            List<Exercise> exercises,
            List<Integer> repetitions) {
        return new Routine(name, exercises, repetitions);
    }

    public static Routine of(
            String name,
            List<Exercise> exercises,
            List<Integer> repetitions,
            List<Workout> workouts) {
        return new Routine(name, exercises, repetitions, workouts);
    }
}
