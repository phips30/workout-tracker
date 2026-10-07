package com.phips30.workouttracker.workout.application.result;

import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;

public record RepetitionResult(String type, int number) {

    public static RepetitionResult from(Repetition repetition) {
        return new RepetitionResult(repetition.getType().name(), repetition.getNumber());
    }
}
