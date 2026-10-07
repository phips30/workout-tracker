package com.phips30.workouttracker.workout.application.result;

import com.phips30.workouttracker.workout.domain.entity.Routine;

import java.util.List;

public record RoutineDetailResult(List<ExerciseResult> exercises, List<RepetitionResult> repetitions) {

    public static RoutineDetailResult from(Routine routine) {
        return new RoutineDetailResult(
                routine.getExercises().stream().map(ExerciseResult::from).toList(),
                routine.getRepetitions().stream().map(RepetitionResult::from).toList());
    }
}
