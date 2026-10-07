package com.phips30.workouttracker.workout.application.result;

import com.phips30.workouttracker.workout.domain.entity.WorkoutSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record WorkoutSessionResult(String id,
                                   LocalDateTime startedAt,
                                   Map<String, Object> metadata,
                                   List<WorkoutSessionEntryResult> entries) {

    public static WorkoutSessionResult from(WorkoutSession workoutSession) {
        return new WorkoutSessionResult(
                workoutSession.getId().getId().toString(),
                workoutSession.getStartedAt(),
                workoutSession.getMetadata(),
                workoutSession.getEntries().stream().map(WorkoutSessionEntryResult::from).toList());
    }
}
