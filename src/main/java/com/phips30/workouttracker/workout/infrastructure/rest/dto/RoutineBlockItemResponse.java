package com.phips30.workouttracker.workout.infrastructure.rest.dto;

public record RoutineBlockItemResponse(int position, ExerciseResponse exercise, RepetitionResponse repetition) {
}
