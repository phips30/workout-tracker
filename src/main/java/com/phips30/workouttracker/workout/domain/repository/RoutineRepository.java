package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.Workout;

import java.util.List;
import java.util.Optional;

public interface RoutineRepository {
    Optional<Routine> loadRoutine(String name);
    boolean exists(String name);
    void saveRoutine(Routine routine);
}
