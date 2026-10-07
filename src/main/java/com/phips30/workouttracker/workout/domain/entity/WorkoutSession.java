package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.util.AssertionHelper;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.RepetitionType;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Aggregate root workout session: one execution of a {@link Routine}, with free-form metadata
 * (e.g. the added weight) and one {@link WorkoutSessionEntry} per performed routine block item and round.
 */
public class WorkoutSession {

    /**
     * Describes what was done for an item. Which value counts depends on the item's repetition type:
     * {@code completedRepetitions} for repetitions, {@code completed} for seconds.
     */
    public record EntryDefinition(int blockPosition,
                                  int round,
                                  int itemPosition,
                                  int completedRepetitions,
                                  boolean completed) {
    }

    private final EntityId id;
    private final EntityId routineId;
    private final LocalDateTime startedAt;
    private final Map<String, Object> metadata;
    private final List<WorkoutSessionEntry> entries;

    private WorkoutSession(EntityId id,
                           EntityId routineId,
                           LocalDateTime startedAt,
                           Map<String, Object> metadata,
                           List<WorkoutSessionEntry> entries) {
        AssertionHelper.assertNotNull(id, "Entity id is null");
        AssertionHelper.assertNotNull(routineId, "Routine id is null");
        AssertionHelper.assertNotNull(startedAt, "StartedAt is null");
        AssertionHelper.assertNotNullOrEmpty(entries, "Entries is null or empty");
        if (entries.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Entries contains null");
        }
        Set<List<Integer>> references = new HashSet<>();
        for (WorkoutSessionEntry entry : entries) {
            if (!references.add(List.of(entry.getBlockPosition(), entry.getRound(), entry.getItemPosition()))) {
                throw new IllegalArgumentException(String.format(
                        "Duplicate entry for block %d, round %d, item %d",
                        entry.getBlockPosition(), entry.getRound(), entry.getItemPosition()));
            }
        }

        this.id = id;
        this.routineId = routineId;
        this.startedAt = startedAt;
        this.metadata = metadata == null
                ? Map.of()
                : Collections.unmodifiableMap(new HashMap<>(metadata));
        this.entries = entries.stream()
                .sorted(Comparator.comparingInt(WorkoutSessionEntry::getBlockPosition)
                        .thenComparingInt(WorkoutSessionEntry::getRound)
                        .thenComparingInt(WorkoutSessionEntry::getItemPosition))
                .toList();
    }

    /**
     * Starts a session of the given routine. Every entry must reference an existing item of the routine
     * and a round within the rounds of its block, and may appear only once.
     */
    public static WorkoutSession createNew(Routine routine,
                                           LocalDateTime startedAt,
                                           Map<String, Object> metadata,
                                           List<EntryDefinition> entryDefinitions) {
        AssertionHelper.assertNotNull(routine, "Routine is null");
        AssertionHelper.assertNotNullOrEmpty(entryDefinitions, "Entries is null or empty");
        List<WorkoutSessionEntry> entries = entryDefinitions.stream()
                .map(definition -> toEntry(routine, definition))
                .toList();
        return new WorkoutSession(EntityId.generate(), routine.getId(), startedAt, metadata, entries);
    }

    public static WorkoutSession of(EntityId id,
                                    EntityId routineId,
                                    LocalDateTime startedAt,
                                    Map<String, Object> metadata,
                                    List<WorkoutSessionEntry> entries) {
        return new WorkoutSession(id, routineId, startedAt, metadata, entries);
    }

    private static WorkoutSessionEntry toEntry(Routine routine, EntryDefinition definition) {
        AssertionHelper.assertNotNull(definition, "Entry is null");
        RoutineBlock block = routine.getBlocks().stream()
                .filter(b -> b.getPosition() == definition.blockPosition())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Routine has no block at position %d", definition.blockPosition())));
        if (definition.round() < 1 || definition.round() > block.getRounds()) {
            throw new IllegalArgumentException(String.format(
                    "Round %d is out of range, block %d has %d rounds",
                    definition.round(), block.getPosition(), block.getRounds()));
        }
        RoutineBlockItem item = block.getItems().stream()
                .filter(i -> i.getPosition() == definition.itemPosition())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Block %d has no item at position %d",
                                block.getPosition(), definition.itemPosition())));

        if (item.getRepetition().getType() == RepetitionType.SECONDS) {
            return WorkoutSessionEntry.ofSeconds(
                    definition.blockPosition(), definition.round(), definition.itemPosition(), definition.completed());
        }
        return WorkoutSessionEntry.ofRepetitions(
                definition.blockPosition(), definition.round(), definition.itemPosition(), definition.completedRepetitions());
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

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public List<WorkoutSessionEntry> getEntries() {
        return entries;
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean equalObjects = false;

        if (otherObject != null && this.getClass() == otherObject.getClass()) {
            WorkoutSession session = (WorkoutSession) otherObject;
            equalObjects = this.id.equals(session.id);
        }
        return equalObjects;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return "WorkoutSession{" +
                "id=" + id +
                ", routineId=" + routineId +
                ", startedAt=" + startedAt +
                '}';
    }
}
