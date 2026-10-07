package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.Round;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutTest {

    private final LocalDateTime startedAt = LocalDateTime.of(
            2025,
            Month.JANUARY,
            1,
            0,
            0,
            0);

    private final EntityId routineId = EntityId.generate();

    private final List<Round> rounds = List.of(
            new Round(Duration.ofMinutes(1)),
            new Round(Duration.ofHours(1)));

    @Test
    public void initWorkoutWithProperValues() {
        Workout workout = Workout.createNew(
                routineId,
                startedAt,
                rounds,
                Map.of()
        );

        assertEquals(routineId, workout.getRoutineId());
        assertNotNull(workout.getId());
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
    public void reconstituteWorkout_keepsGivenId() {
        EntityId id = EntityId.generate();

        Workout workout = Workout.of(id, routineId, startedAt, rounds, Map.of());

        assertEquals(id, workout.getId());
        assertEquals(routineId, workout.getRoutineId());
    }

    @Test
    public void initWorkoutWithNoRoutineId_throwsException() {
        try {
            Workout.createNew(
                    null,
                    startedAt,
                    rounds,
                    null
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Routine id is null", e.getMessage());
        }
    }

    @Test
    public void initWorkoutWithNoStartTime_throwsException() {
        try {
            Workout.createNew(
                    routineId,
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
    public void initWorkoutWithEmptyRounds_throwsException() {
        try {
            Workout.createNew(
                    routineId,
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
    public void initWorkoutWithNoRounds_throwsException() {
        try {
            Workout.createNew(
                    routineId,
                    startedAt,
                    null,
                    null
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Rounds is null or empty", e.getMessage());
        }
    }

    @Test
    public void initWorkoutWithNoMetadata_returnsEmptyMetadata() {
        Workout workout = Workout.createNew(routineId, startedAt, rounds, null);

        assertTrue(workout.getMetadata().isEmpty());
    }

    @Test
    public void changingInputCollectionsAfterCreation_doesNotAffectWorkout() {
        List<Round> mutableRounds = new ArrayList<>(rounds);
        Map<String, Object> mutableMetadata = new HashMap<>();
        mutableMetadata.put("note", "good");

        Workout workout = Workout.createNew(routineId, startedAt, mutableRounds, mutableMetadata);
        mutableRounds.add(new Round(Duration.ofHours(5)));
        mutableMetadata.put("other", "value");

        assertEquals(2, workout.getRounds().size());
        assertEquals(Map.of("note", "good"), workout.getMetadata());
    }

    @Test
    public void exposedCollectionsCannotBeModified() {
        Workout workout = Workout.createNew(routineId, startedAt, rounds, Map.of("note", "good"));

        assertThrows(UnsupportedOperationException.class,
                () -> workout.getRounds().add(new Round(Duration.ofMinutes(1))));
        assertThrows(UnsupportedOperationException.class,
                () -> workout.getMetadata().put("other", "value"));
    }

    @Test
    public void workoutsWithSameId_areEqual() {
        EntityId id = EntityId.generate();

        Workout first = Workout.of(id, routineId, startedAt, rounds, Map.of());
        Workout second = Workout.of(id, routineId, startedAt.plusDays(1), rounds, Map.of());

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first, Workout.createNew(routineId, startedAt, rounds, Map.of()));
    }
}
