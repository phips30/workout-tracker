package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.Exercise;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ExerciseRepository {
    boolean exists(String name);
    Exercise save(Exercise exercise);
    List<Exercise> loadAll();
    List<Exercise> loadByIds(Set<UUID> exerciseIds);
}
