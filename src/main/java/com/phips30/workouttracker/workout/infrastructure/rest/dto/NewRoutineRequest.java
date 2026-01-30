package com.phips30.workouttracker.workout.infrastructure.rest.dto;

import com.phips30.workouttracker.workout.domain.entity.RoutineType;

import java.util.List;

public record NewRoutineRequest(String name, RoutineType routineType, List<String> exerciseIds,
                                List<Integer> repetitions) {
}
