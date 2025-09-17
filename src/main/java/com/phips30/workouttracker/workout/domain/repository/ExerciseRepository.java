package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.Exercise;

public interface ExerciseRepository {
    boolean exists(String name);
    Exercise save(Exercise exercise);
}
