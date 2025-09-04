package com.phips30.workouttracker.workout.domain.entity;


import com.phips30.workouttracker.workout.domain.valueobjects.Round;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Aggregate root workout
 */
public class Workout {
    private final LocalDateTime startedAt;
    private final LocalDateTime completedAt;
    private final List<Round> rounds;
    private final List<Pair<String, String>> metadata;

    private Workout(LocalDateTime startedAt, List<Round> rounds, List<Pair<String, String>> metadata) {
        if (startedAt == null) {
            throw new IllegalArgumentException("StartedAt is empty");
        }
        if (rounds == null || rounds.isEmpty()) {
            throw new IllegalArgumentException("Rounds is null or empty");
        }

        this.startedAt = startedAt;
        this.rounds = rounds;
        this.metadata = metadata;
        this.completedAt = startedAt.plus(getTotalDuration());
    }

    public static Workout of(LocalDateTime startedAt, List<Round> rounds, List<Pair<String, String>> metadata) {
        return new Workout(startedAt, rounds, metadata);
    }

    private Duration getTotalDuration() {
        return rounds.stream().map(Round::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}
