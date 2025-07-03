package com.phips30.workouttracker.workout.infrastructure.rest.dto;

import lombok.Getter;
import lombok.Value;

@Value
@Getter
public class NewExerciseRequest {
    private String name;

    public String getName() {
        return name;
    }
}
