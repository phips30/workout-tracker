package com.phips30.workouttracker.workout.domain.entity;


import com.phips30.workouttracker.workout.domain.util.AssertionHelper;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.Round;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Aggregate root workout
 */
public class Workout {
    private final EntityId id;
    private final LocalDateTime startedAt;
    private final LocalDateTime completedAt;
    private final List<Round> rounds;
    private final Map<String, Object> metadata;

    private Workout(EntityId id, LocalDateTime startedAt, List<Round> rounds, Map<String, Object> metadata) {
        AssertionHelper.assertNotNull(id, "Entity id is null");
        if (startedAt == null) {
            throw new IllegalArgumentException("StartedAt is empty");
        }
        if (rounds == null || rounds.isEmpty()) {
            throw new IllegalArgumentException("Rounds is null or empty");
        }

        this.id = id;
        this.startedAt = startedAt;
        this.rounds = rounds;
        this.metadata = metadata;
        this.completedAt = startedAt.plus(getTotalDuration());
    }

    public static Workout of(LocalDateTime startedAt,
                             List<Round> rounds,
                             Map<String, Object> metadata) {
        return new Workout(EntityId.generate(), startedAt, rounds, metadata);
    }

    private Duration getTotalDuration() {
        return rounds.stream().map(Round::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public EntityId getId() {
        return id;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
