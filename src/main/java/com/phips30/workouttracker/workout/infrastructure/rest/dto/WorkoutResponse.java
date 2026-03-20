package com.phips30.workouttracker.workout.infrastructure.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record WorkoutResponse(
        String id,
        LocalDateTime startedAt,
        List<Long> roundDuration,
        Map<String, Object> metadata
) {}
