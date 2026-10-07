package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.util.AssertionHelper;
import com.phips30.workouttracker.workout.domain.valueobjects.RepetitionType;

import java.util.Objects;

/**
 * Part of a {@link WorkoutSession}: what was done for one {@link RoutineBlockItem} in one round of its block.
 * The item is referenced by its block position, item position and round, as items have no identity of their own.
 * For items counted in {@link RepetitionType#NUMBER} it holds the completed repetitions, for items measured
 * in {@link RepetitionType#SECONDS} only whether the item was completed.
 */
public final class WorkoutSessionEntry {
    private final int blockPosition;
    private final int round;
    private final int itemPosition;
    private final RepetitionType type;
    private final int completedRepetitions;

    private WorkoutSessionEntry(int blockPosition,
                                int round,
                                int itemPosition,
                                RepetitionType type,
                                int completedRepetitions) {
        if (blockPosition < 1) {
            throw new IllegalArgumentException("Block position must be greater or equal to 1");
        }
        if (round < 1) {
            throw new IllegalArgumentException("Round must be greater or equal to 1");
        }
        if (itemPosition < 1) {
            throw new IllegalArgumentException("Item position must be greater or equal to 1");
        }
        AssertionHelper.assertNotNull(type, "RepetitionType is null");
        if (completedRepetitions < 0) {
            throw new IllegalArgumentException("Completed repetitions must not be negative");
        }
        if (type == RepetitionType.SECONDS && completedRepetitions > 1) {
            throw new IllegalArgumentException("Completed repetitions of a seconds item must be 0 or 1");
        }

        this.blockPosition = blockPosition;
        this.round = round;
        this.itemPosition = itemPosition;
        this.type = type;
        this.completedRepetitions = completedRepetitions;
    }

    /**
     * Entry for an item counted in repetitions
     */
    public static WorkoutSessionEntry ofRepetitions(int blockPosition, int round, int itemPosition, int completedRepetitions) {
        return new WorkoutSessionEntry(blockPosition, round, itemPosition, RepetitionType.NUMBER, completedRepetitions);
    }

    /**
     * Entry for an item measured in seconds, which is either completed or not
     */
    public static WorkoutSessionEntry ofSeconds(int blockPosition, int round, int itemPosition, boolean completed) {
        return new WorkoutSessionEntry(blockPosition, round, itemPosition, RepetitionType.SECONDS, completed ? 1 : 0);
    }

    /**
     * Reconstitutes an entry from storage. For {@link RepetitionType#SECONDS} the amount is 1 (completed) or 0.
     */
    public static WorkoutSessionEntry of(int blockPosition, int round, int itemPosition, RepetitionType type, int amount) {
        return new WorkoutSessionEntry(blockPosition, round, itemPosition, type, amount);
    }

    public int getBlockPosition() {
        return blockPosition;
    }

    public int getRound() {
        return round;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public RepetitionType getType() {
        return type;
    }

    /**
     * Completed repetitions for {@link RepetitionType#NUMBER} items, 1 or 0 for {@link RepetitionType#SECONDS} items
     */
    public int getCompletedRepetitions() {
        return completedRepetitions;
    }

    /**
     * Whether a {@link RepetitionType#SECONDS} item was completed, always false for other items
     */
    public boolean isCompleted() {
        return type == RepetitionType.SECONDS && completedRepetitions == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutSessionEntry that = (WorkoutSessionEntry) o;
        return blockPosition == that.blockPosition
                && round == that.round
                && itemPosition == that.itemPosition
                && completedRepetitions == that.completedRepetitions
                && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockPosition, round, itemPosition, type, completedRepetitions);
    }

    @Override
    public String toString() {
        return "WorkoutSessionEntry{" +
                "blockPosition=" + blockPosition +
                ", round=" + round +
                ", itemPosition=" + itemPosition +
                ", type=" + type +
                ", completedRepetitions=" + completedRepetitions +
                '}';
    }
}
