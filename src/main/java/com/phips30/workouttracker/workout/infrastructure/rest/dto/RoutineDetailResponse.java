package com.phips30.workouttracker.workout.infrastructure.rest.dto;

import java.util.List;

public record RoutineDetailResponse(
        List<ExerciseResponse> exercises,
        List<RepetitionResponse> repetitions) {
}
