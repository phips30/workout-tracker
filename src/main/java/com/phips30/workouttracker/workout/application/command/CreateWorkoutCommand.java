package com.phips30.workouttracker.workout.application.command;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record CreateWorkoutCommand(String routineName,
                                   LocalDateTime startedAt,
                                   List<Duration> roundDurations,
                                   Map<String, Object> metadata) {
}
