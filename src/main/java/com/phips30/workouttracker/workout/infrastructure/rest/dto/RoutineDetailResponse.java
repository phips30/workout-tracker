package com.phips30.workouttracker.workout.infrastructure.rest.dto;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;

import java.util.List;

public record RoutineDetailResponse(
        List<Exercise> exercises,
        List<Repetition> repetitions) {
}