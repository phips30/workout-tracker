package com.phips30.workouttracker.workout.application.result;

import com.phips30.workouttracker.workout.domain.entity.RoutineBlock;

import java.util.List;

public record RoutineBlockResult(int position, int rounds, List<RoutineBlockItemResult> items) {

    public static RoutineBlockResult from(RoutineBlock block) {
        return new RoutineBlockResult(
                block.getPosition(),
                block.getRounds(),
                block.getItems().stream().map(RoutineBlockItemResult::from).toList());
    }
}
