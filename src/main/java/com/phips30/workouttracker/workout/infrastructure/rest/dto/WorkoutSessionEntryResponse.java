package com.phips30.workouttracker.workout.infrastructure.rest.dto;

public record WorkoutSessionEntryResponse(int blockPosition,
                                          int round,
                                          int itemPosition,
                                          String repetitionType,
                                          Integer completedRepetitions,
                                          Boolean completed) {
}
