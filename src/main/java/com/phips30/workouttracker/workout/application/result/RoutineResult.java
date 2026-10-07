package com.phips30.workouttracker.workout.application.result;

import com.phips30.workouttracker.workout.domain.entity.Routine;

public record RoutineResult(String name, String routineType) {

    public static RoutineResult from(Routine routine) {
        return new RoutineResult(routine.getName().getValue(), routine.getRoutineType().name());
    }
}
