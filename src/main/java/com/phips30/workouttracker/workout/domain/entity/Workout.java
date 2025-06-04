package com.phips30.workouttracker.workout.domain.entity;


import com.phips30.workouttracker.workout.domain.valueobjects.Round;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.List;

public class Workout {
    private LocalDateTime completedAt;
    private List<Round> rounds;
    private List<Pair<String, String>> metadata;

    private Workout(LocalDateTime completedAt, List<Round> rounds, List<Pair<String, String>> metadata) {
        if (completedAt == null) {
            throw new IllegalArgumentException("CompletedAt is empty");
        }
        if (rounds == null || rounds.isEmpty()) {
            throw new RuntimeException("Rounds is null or empty");
        }

        this.completedAt = completedAt;
        this.rounds = rounds;
        this.metadata = metadata;
    }

    public static Workout of(LocalDateTime completedAt, List<Round> rounds, List<Pair<String, String>> metadata) {
        return new Workout(completedAt, rounds, metadata);
    }
}
