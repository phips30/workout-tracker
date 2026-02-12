package com.phips30.workouttracker.workout.infrastructure.rest.dto;

import com.phips30.workouttracker.workout.domain.valueobjects.Round;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.List;

public record NewWorkoutRequest(LocalDateTime startedAt,
                                List<Round> rounds,
                                List<Pair<String, String>> metadata) {
}
