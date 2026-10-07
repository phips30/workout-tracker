package com.phips30.workouttracker.workout.application.result;

import com.phips30.workouttracker.workout.domain.entity.Exercise;

public record ExerciseResult(String id, String name) {

    public static ExerciseResult from(Exercise exercise) {
        return new ExerciseResult(exercise.getId().getId().toString(), exercise.getName().getValue());
    }
}
