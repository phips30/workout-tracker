package com.phips30.workouttracker.workout.infrastructure.rest.dto;

/**
 * {@code completedRepetitions} applies to items counted in repetitions, {@code completed} to items measured
 * in seconds.
 */
public record WorkoutSessionEntryRequest(int blockPosition,
                                         int round,
                                         int itemPosition,
                                         Integer completedRepetitions,
                                         Boolean completed) {
}
