package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.Routine;

import java.util.Optional;
import java.util.Set;

public interface RoutineRepository {
    Optional<Routine> loadRoutine(String name);
    Set<Routine> loadRoutines();
    boolean exists(String name);
    void saveRoutine(Routine routine);
}
