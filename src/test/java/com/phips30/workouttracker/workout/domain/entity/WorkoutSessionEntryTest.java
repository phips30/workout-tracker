package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.RepetitionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutSessionEntryTest {

    @Test
    public void ofRepetitions_keepsCompletedRepetitions() {
        WorkoutSessionEntry entry = WorkoutSessionEntry.ofRepetitions(1, 2, 3, 8);

        assertEquals(1, entry.getBlockPosition());
        assertEquals(2, entry.getRound());
        assertEquals(3, entry.getItemPosition());
        assertEquals(RepetitionType.NUMBER, entry.getType());
        assertEquals(8, entry.getCompletedRepetitions());
        assertFalse(entry.isCompleted());
    }

    @Test
    public void ofRepetitions_zeroRepetitionsAreAllowed() {
        assertEquals(0, WorkoutSessionEntry.ofRepetitions(1, 1, 1, 0).getCompletedRepetitions());
    }

    @Test
    public void ofSeconds_tracksWhetherItemWasCompleted() {
        WorkoutSessionEntry completed = WorkoutSessionEntry.ofSeconds(1, 1, 1, true);
        WorkoutSessionEntry notCompleted = WorkoutSessionEntry.ofSeconds(1, 1, 1, false);

        assertEquals(RepetitionType.SECONDS, completed.getType());
        assertTrue(completed.isCompleted());
        assertFalse(notCompleted.isCompleted());
    }

    @Test
    public void of_reconstitutesEntry() {
        assertEquals(WorkoutSessionEntry.ofSeconds(1, 2, 3, true),
                WorkoutSessionEntry.of(1, 2, 3, RepetitionType.SECONDS, 1));
        assertEquals(WorkoutSessionEntry.ofRepetitions(1, 2, 3, 5),
                WorkoutSessionEntry.of(1, 2, 3, RepetitionType.NUMBER, 5));
    }

    @Test
    public void invalidValues_throwException() {
        assertThrows(IllegalArgumentException.class, () -> WorkoutSessionEntry.ofRepetitions(0, 1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> WorkoutSessionEntry.ofRepetitions(1, 0, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> WorkoutSessionEntry.ofRepetitions(1, 1, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> WorkoutSessionEntry.ofRepetitions(1, 1, 1, -1));
        assertThrows(IllegalArgumentException.class, () -> WorkoutSessionEntry.of(1, 1, 1, null, 1));
        assertThrows(IllegalArgumentException.class, () -> WorkoutSessionEntry.of(1, 1, 1, RepetitionType.SECONDS, 2));
    }

    @Test
    public void entriesWithSameValues_areEqual() {
        WorkoutSessionEntry first = WorkoutSessionEntry.ofRepetitions(1, 1, 1, 5);

        assertEquals(first, WorkoutSessionEntry.ofRepetitions(1, 1, 1, 5));
        assertEquals(first.hashCode(), WorkoutSessionEntry.ofRepetitions(1, 1, 1, 5).hashCode());
        assertNotEquals(first, WorkoutSessionEntry.ofRepetitions(1, 1, 1, 6));
    }
}
