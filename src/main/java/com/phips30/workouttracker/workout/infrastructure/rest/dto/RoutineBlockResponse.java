package com.phips30.workouttracker.workout.infrastructure.rest.dto;

import java.util.List;

public record RoutineBlockResponse(int position, int rounds, List<RoutineBlockItemResponse> items) {
}
