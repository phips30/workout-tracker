package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Routine {
    private String name;
    private List<Exercise> exercises;
    private List<Repetition> repetitions;
    private List<Workout> workouts;

    private Routine(String name, List<Exercise> exercises, List<Repetition> repetitions) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Routine name is null or empty");
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

        this.name = name;
        this.exercises = exercises;
        this.repetitions = repetitions;
        this.workouts = new ArrayList<>();
    }

    private Routine(String name, List<Exercise> exercises, List<Repetition> repetitions, List<Workout> workouts) {
        this(name, exercises, repetitions);
        this.workouts = workouts;
    }

    public static Routine of(
            String name,
            List<Exercise> exercises,
            List<Repetition> repetitions) {
        return new Routine(name, exercises, repetitions);
    }

    public static Routine of(
            String name,
            List<Exercise> exercises,
            List<Repetition> repetitions,
            List<Workout> workouts) {
        return new Routine(name, exercises, repetitions, workouts);
    }

    public List<Exercise> getExercises() {
        return Collections.unmodifiableList(this.exercises);
    }

    public List<Repetition> getRepetitions() {
        return Collections.unmodifiableList(this.repetitions);
    }

    public List<Workout> getWorkouts() {
        return Collections.unmodifiableList(this.workouts);
    }

    public String getName() {
        return this.name;
    }
}
