package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.Workout;

import java.util.Set;

public interface WorkoutRepository {
    Set<Workout> loadWorkoutsForRoutine(Routine routine);
}
