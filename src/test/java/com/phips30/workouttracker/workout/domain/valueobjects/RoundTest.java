package com.phips30.workouttracker.workout.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @Test
    public void init_noDuration_throwsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Round(null));
        assertEquals("Duration is null", exception.getMessage());
    }

    @Test
    public void equalsAndHashCode_sameDuration_areEqual() {
        assertEquals(new Round(Duration.ofMinutes(5)), new Round(Duration.ofMinutes(5)));
        assertEquals(new Round(Duration.ofMinutes(5)).hashCode(), new Round(Duration.ofMinutes(5)).hashCode());
        assertNotEquals(new Round(Duration.ofMinutes(5)), new Round(Duration.ofMinutes(6)));
    }
}
