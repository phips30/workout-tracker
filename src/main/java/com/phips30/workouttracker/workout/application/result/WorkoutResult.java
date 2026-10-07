package com.phips30.workouttracker.workout.application.result;

import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.valueobjects.Round;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record WorkoutResult(String id,
                            LocalDateTime startedAt,
                            List<Duration> roundDurations,
                            Map<String, Object> metadata) {

    public static WorkoutResult from(Workout workout) {
        return new WorkoutResult(
                workout.getId().getId().toString(),
                workout.getStartedAt(),
                workout.getRounds().stream().map(Round::getDuration).toList(),
                workout.getMetadata());
    }
}
