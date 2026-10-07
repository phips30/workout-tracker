package com.phips30.workouttracker.workout.application.result;

import com.phips30.workouttracker.workout.domain.entity.WorkoutSessionEntry;
import com.phips30.workouttracker.workout.domain.valueobjects.RepetitionType;

/**
 * {@code completedRepetitions} is set for items counted in repetitions, {@code completed} for items measured
 * in seconds; the other one is null.
 */
public record WorkoutSessionEntryResult(int blockPosition,
                                        int round,
                                        int itemPosition,
                                        String repetitionType,
                                        Integer completedRepetitions,
                                        Boolean completed) {

    public static WorkoutSessionEntryResult from(WorkoutSessionEntry entry) {
        boolean seconds = entry.getType() == RepetitionType.SECONDS;
        return new WorkoutSessionEntryResult(
                entry.getBlockPosition(),
                entry.getRound(),
                entry.getItemPosition(),
                entry.getType().name(),
                seconds ? null : entry.getCompletedRepetitions(),
                seconds ? entry.isCompleted() : null);
    }
}
