package com.phips30.workouttracker.workout.application.result;

import com.phips30.workouttracker.workout.domain.entity.Routine;

import java.util.List;

public record RoutineDetailResult(List<RoutineBlockResult> blocks) {

    public static RoutineDetailResult from(Routine routine) {
        return new RoutineDetailResult(routine.getBlocks().stream().map(RoutineBlockResult::from).toList());
    }
}
