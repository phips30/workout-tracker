package com.phips30.workouttracker.workout.domain.entity;


import com.phips30.workouttracker.workout.domain.util.AssertionHelper;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.Round;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Aggregate root workout
 */
public class Workout {
    private final EntityId id;
    private final EntityId routineId;
    private final LocalDateTime startedAt;
    private final LocalDateTime completedAt;
    private final List<Round> rounds;
    private final Map<String, Object> metadata;

    private Workout(EntityId id,
                    EntityId routineId,
                    LocalDateTime startedAt,
                    List<Round> rounds,
                    Map<String, Object> metadata) {
        AssertionHelper.assertNotNull(id, "Entity id is null");
        AssertionHelper.assertNotNull(routineId, "Routine id is null");
        if (startedAt == null) {
            throw new IllegalArgumentException("StartedAt is empty");
        }
        if (rounds == null || rounds.isEmpty()) {
            throw new IllegalArgumentException("Rounds is null or empty");
        }

        this.id = id;
        this.routineId = routineId;
        this.startedAt = startedAt;
        this.rounds = List.copyOf(rounds);
        this.metadata = metadata == null
                ? Map.of()
                : Collections.unmodifiableMap(new HashMap<>(metadata));
        this.completedAt = startedAt.plus(getTotalDuration());
    }

    public static Workout createNew(EntityId routineId,
                                    LocalDateTime startedAt,
                                    List<Round> rounds,
                                    Map<String, Object> metadata) {
        return new Workout(EntityId.generate(), routineId, startedAt, rounds, metadata);
    }

    public static Workout of(EntityId id,
                             EntityId routineId,
                             LocalDateTime startedAt,
                             List<Round> rounds,
                             Map<String, Object> metadata) {
        return new Workout(id, routineId, startedAt, rounds, metadata);
    }

    private Duration getTotalDuration() {
        return rounds.stream().map(Round::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public EntityId getId() {
        return id;
    }

    public EntityId getRoutineId() {
        return routineId;
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

    @Override
    public boolean equals(Object otherObject) {
        boolean equalObjects = false;

        if (otherObject != null && this.getClass() == otherObject.getClass()) {
            Workout workout = (Workout) otherObject;
            equalObjects = this.id.equals(workout.id);
        }
        return equalObjects;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", routineId=" + routineId +
                ", startedAt=" + startedAt +
                '}';
    }
}
