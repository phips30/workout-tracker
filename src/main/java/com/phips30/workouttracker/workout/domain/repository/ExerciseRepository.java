package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;

import java.util.List;

public interface ExerciseRepository {
    boolean exists(ExerciseName name);
    Exercise save(Exercise exercise);
    List<Exercise> loadAll();
    List<Exercise> loadByIds(List<EntityId> exerciseIds);
}
