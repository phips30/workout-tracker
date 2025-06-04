package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.Routine;

public interface WorkoutRepository {
    Routine loadRoutine(String name);
}
