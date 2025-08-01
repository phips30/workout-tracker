package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.Workout;

import java.util.List;

public interface WorkoutRepository {
    List<Workout> loadWorkoutsForRoutine(Routine routine);
}
