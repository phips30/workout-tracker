package com.phips30.workouttracker.workout.infrastructure.rest.dto;

import java.util.List;

public record NewRoutineRequest(String name, String routineType, List<RoutineBlockRequest> blocks) {
}
