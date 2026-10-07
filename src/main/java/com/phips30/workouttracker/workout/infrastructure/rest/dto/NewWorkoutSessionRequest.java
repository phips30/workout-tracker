package com.phips30.workouttracker.workout.infrastructure.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record NewWorkoutSessionRequest(LocalDateTime startedAt,
                                       Map<String, Object> metadata,
                                       List<WorkoutSessionEntryRequest> entries) {
}
