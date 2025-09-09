package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.Routine;

import java.util.List;
import java.util.Optional;

public interface RoutineRepository {
    Optional<Routine> loadRoutine(String name);
    List<Routine> loadRoutines();
    boolean exists(String name);
    void saveRoutine(Routine routine);
}
