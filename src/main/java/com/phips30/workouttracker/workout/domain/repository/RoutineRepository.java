package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;

import java.util.List;
import java.util.Optional;

public interface RoutineRepository {
    Optional<Routine> loadRoutine(RoutineName name);
    List<Routine> loadRoutines();
    boolean exists(RoutineName name);
    void saveRoutine(Routine routine);
}
