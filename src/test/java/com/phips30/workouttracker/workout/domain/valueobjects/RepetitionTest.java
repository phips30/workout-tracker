package com.phips30.workouttracker.workout.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepetitionTest {

    @Test
    public void of_number_createsNumberRepetition() {
        Repetition repetition = Repetition.of(10);

        assertEquals(10, repetition.getNumber());
        assertEquals(RepetitionType.NUMBER, repetition.getType());
    }

    @Test
    public void of_typeAndNumber_createsRepetitionOfType() {
        Repetition repetition = Repetition.of(RepetitionType.SECONDS, 30);

        assertEquals(30, repetition.getNumber());
        assertEquals(RepetitionType.SECONDS, repetition.getType());
    }

    @Test
    public void of_zeroOrNegativeNumber_throwsException() {
        IllegalArgumentException zero = assertThrows(IllegalArgumentException.class, () -> Repetition.of(0));
        assertEquals("Number must be greater or equal to 1", zero.getMessage());
        assertThrows(IllegalArgumentException.class, () -> Repetition.of(-5));
    }

    @Test
    public void of_noType_throwsException() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Repetition.of(null, 5));
        assertEquals("RepetitionType is null", exception.getMessage());
    }

    @Test
    public void equalsAndHashCode_sameTypeAndNumber_areEqual() {
        assertEquals(Repetition.of(10), Repetition.of(10));
        assertEquals(Repetition.of(10).hashCode(), Repetition.of(10).hashCode());
        assertNotEquals(Repetition.of(10), Repetition.of(11));
        assertNotEquals(Repetition.of(10), Repetition.of(RepetitionType.SECONDS, 10));
    }
}
