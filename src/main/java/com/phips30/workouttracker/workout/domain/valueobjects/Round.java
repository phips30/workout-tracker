package com.phips30.workouttracker.workout.domain.valueobjects;

import java.time.Duration;
import java.util.Objects;

public final class Round {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return duration.equals(round.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration);
    }

    @Override
    public String toString() {
        return "Round{" +
                "duration=" + duration +
                '}';
    }
}
