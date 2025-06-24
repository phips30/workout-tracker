package com.phips30.workouttracker.workout.domain.valueobjects;

import java.time.Duration;

public class Round {
    private final Duration duration;

    public Round(Duration duration) {
        if(duration == null) {
            throw new IllegalArgumentException("Duration is null");
        }
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }
}
