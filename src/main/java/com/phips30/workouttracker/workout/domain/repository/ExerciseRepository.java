package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.Exercise;

import java.util.List;

public interface ExerciseRepository {
    boolean exists(String name);
    Exercise save(Exercise exercise);
    List<Exercise> loadAll();
}
