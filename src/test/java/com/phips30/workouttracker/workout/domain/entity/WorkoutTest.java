package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.Round;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutTest {

    private final LocalDateTime startedAt = LocalDateTime.of(
            2025,
            Month.JANUARY,
            1,
            0,
            0,
            0);

    private final List<Round> rounds = List.of(
            new Round(Duration.ofMinutes(1)),
            new Round(Duration.ofHours(1)));

    @Test
    public void initWorkoutWithProperValues() {
        Workout workout = Workout.of(
                startedAt,
                rounds,
                new ArrayList<>()
        );

        assertEquals(LocalDateTime.of(
                2025,
                Month.JANUARY,
                1,
                1,
                1,
                0
        ), workout.getCompletedAt());
    }

    @Test
    public void initWorkoutWithNoStartTime_throwsException() {
        try {
            Workout.of(
                    null,
                    rounds,
                    null
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("StartedAt is empty", e.getMessage());
        }
    }

    @Test
    public void initWorkoutWithNoRounds_throwsException() {
        try {
            Workout.of(
                    startedAt,
                    new ArrayList<>(),
                    null
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Rounds is null or empty", e.getMessage());
        }
    }

    @Test
    public void initWorkoutWithEmptyRounds_throwsException() {
        try {
            Workout.of(
                    startedAt,
                    null,
                    null
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Rounds is null or empty", e.getMessage());
        }
    }
}