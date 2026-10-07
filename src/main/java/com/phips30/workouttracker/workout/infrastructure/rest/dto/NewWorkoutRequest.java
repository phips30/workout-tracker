package com.phips30.workouttracker.workout.infrastructure.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record NewWorkoutRequest(LocalDateTime startedAt,
                                List<RoundRequest> rounds,
                                Map<String, Object> metadata) {
}
