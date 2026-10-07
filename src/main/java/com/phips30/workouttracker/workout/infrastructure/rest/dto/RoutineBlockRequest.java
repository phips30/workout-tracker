package com.phips30.workouttracker.workout.infrastructure.rest.dto;

import java.util.List;

public record RoutineBlockRequest(int position, int rounds, List<RoutineBlockItemRequest> items) {
}
