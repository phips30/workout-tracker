package com.phips30.workouttracker.workout.domain.repository;

import com.phips30.workouttracker.workout.domain.entity.WorkoutSession;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;

import java.util.List;

public interface WorkoutSessionRepository {
    List<WorkoutSession> loadForRoutine(EntityId routineId);
    WorkoutSession save(WorkoutSession workoutSession);
}
