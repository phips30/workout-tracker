package com.phips30.workouttracker.workout.infrastructure.rest.dto;

import com.phips30.workouttracker.workout.domain.valueobjects.Round;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record NewWorkoutRequest(LocalDateTime startedAt,
                                List<Round> rounds,
                                Map<String, Object> metadata) {
}
