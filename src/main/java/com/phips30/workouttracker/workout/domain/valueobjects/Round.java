package com.phips30.workouttracker.workout.domain.valueobjects;

import java.time.Duration;

public class Round {
    private int round;
    private Duration duration;

    public Round(int round, Duration duration) {
        if(duration == null) {
            throw new IllegalArgumentException("Duration is null");
        }
        this.round = round;
        this.duration = duration;
    }
}
