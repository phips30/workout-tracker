package com.phips30.workouttracker.workout.application.result;

import com.phips30.workouttracker.workout.domain.entity.RoutineBlockItem;

public record RoutineBlockItemResult(int position, ExerciseResult exercise, RepetitionResult repetition) {

    public static RoutineBlockItemResult from(RoutineBlockItem item) {
        return new RoutineBlockItemResult(
                item.getPosition(),
                ExerciseResult.from(item.getExercise()),
                RepetitionResult.from(item.getRepetition()));
    }
}
