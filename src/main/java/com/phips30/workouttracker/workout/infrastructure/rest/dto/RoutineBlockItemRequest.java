package com.phips30.workouttracker.workout.infrastructure.rest.dto;

public record RoutineBlockItemRequest(int position, String exerciseId, String repetitionType, int repetitions) {
}
