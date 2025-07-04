package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.Exercise;

import java.util.Optional;

public interface ExerciseRepository {
    boolean exists(String name);

    Optional<Exercise> create(Exercise exercise);
}
