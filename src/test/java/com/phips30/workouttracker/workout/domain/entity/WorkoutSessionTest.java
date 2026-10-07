package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RepetitionType;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutSessionTest {

    private final LocalDateTime startedAt = LocalDateTime.of(2025, 1, 1, 10, 0);

    // block 1 (2 rounds): item 1 = 10 repetitions, item 2 = 30 seconds
    private final Routine routine = Routine.createNew(
            new RoutineName("routine"),
            RoutineType.AMRAP,
            List.of(RoutineBlock.of(1, 2, List.of(
                    RoutineBlockItem.of(1, new Exercise(new com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName("push-up")), Repetition.of(10)),
                    RoutineBlockItem.of(2, new Exercise(new com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName("plank")), Repetition.of(RepetitionType.SECONDS, 30))))));

    private WorkoutSession.EntryDefinition entry(int round, int item, int repetitions, boolean completed) {
        return new WorkoutSession.EntryDefinition(1, round, item, repetitions, completed);
    }

    @Test
    public void createNew_mapsEntriesToRoutineItemsByTheirRepetitionType() {
        WorkoutSession session = WorkoutSession.createNew(routine, startedAt, Map.of("weight", 12.5), List.of(
                entry(1, 1, 8, true),
                entry(1, 2, 0, true),
                entry(2, 1, 10, false),
                entry(2, 2, 30, false)));

        assertNotNull(session.getId());
        assertEquals(routine.getId(), session.getRoutineId());
        assertEquals(startedAt, session.getStartedAt());
        assertEquals(Map.of("weight", 12.5), session.getMetadata());
        assertEquals(List.of(
                WorkoutSessionEntry.ofRepetitions(1, 1, 1, 8),
                WorkoutSessionEntry.ofSeconds(1, 1, 2, true),
                WorkoutSessionEntry.ofRepetitions(1, 2, 1, 10),
                WorkoutSessionEntry.ofSeconds(1, 2, 2, false)), session.getEntries());
    }

    @Test
    public void reconstituteSession_keepsGivenId() {
        EntityId id = EntityId.generate();
        EntityId routineId = EntityId.generate();

        WorkoutSession session = WorkoutSession.of(id, routineId, startedAt, null,
                List.of(WorkoutSessionEntry.ofRepetitions(1, 1, 1, 5)));

        assertEquals(id, session.getId());
        assertEquals(routineId, session.getRoutineId());
        assertTrue(session.getMetadata().isEmpty());
    }

    @Test
    public void createNew_noRoutine_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkoutSession.createNew(null, startedAt, Map.of(), List.of(entry(1, 1, 1, false))));
    }

    @Test
    public void createNew_noStartTime_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> WorkoutSession.createNew(routine, null, Map.of(), List.of(entry(1, 1, 1, false))));
        assertEquals("StartedAt is null", e.getMessage());
    }

    @Test
    public void createNew_noEntries_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkoutSession.createNew(routine, startedAt, Map.of(), null));
        assertThrows(IllegalArgumentException.class,
                () -> WorkoutSession.createNew(routine, startedAt, Map.of(), List.of()));
    }

    @Test
    public void createNew_unknownBlock_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> WorkoutSession.createNew(
                routine, startedAt, Map.of(), List.of(new WorkoutSession.EntryDefinition(2, 1, 1, 1, false))));
        assertEquals("Routine has no block at position 2", e.getMessage());
    }

    @Test
    public void createNew_unknownItem_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkoutSession.createNew(routine, startedAt, Map.of(), List.of(entry(1, 3, 1, false))));
    }

    @Test
    public void createNew_roundOutOfRange_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkoutSession.createNew(routine, startedAt, Map.of(), List.of(entry(3, 1, 1, false))));
        assertThrows(IllegalArgumentException.class,
                () -> WorkoutSession.createNew(routine, startedAt, Map.of(), List.of(entry(0, 1, 1, false))));
    }

    @Test
    public void createNew_duplicateEntry_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> WorkoutSession.createNew(
                routine, startedAt, Map.of(), List.of(entry(1, 1, 5, false), entry(1, 1, 6, false))));
        assertEquals("Duplicate entry for block 1, round 1, item 1", e.getMessage());
    }

    @Test
    public void createNew_negativeRepetitions_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> WorkoutSession.createNew(routine, startedAt, Map.of(), List.of(entry(1, 1, -1, false))));
    }

    @Test
    public void changingInputCollectionsAfterCreation_doesNotAffectSession() {
        List<WorkoutSessionEntry> entries = new ArrayList<>(List.of(WorkoutSessionEntry.ofRepetitions(1, 1, 1, 5)));
        Map<String, Object> metadata = new HashMap<>(Map.of("note", "good"));

        WorkoutSession session = WorkoutSession.of(EntityId.generate(), EntityId.generate(), startedAt, metadata, entries);
        entries.add(WorkoutSessionEntry.ofRepetitions(1, 1, 2, 5));
        metadata.put("other", "value");

        assertEquals(1, session.getEntries().size());
        assertEquals(Map.of("note", "good"), session.getMetadata());
    }

    @Test
    public void exposedCollectionsCannotBeModified() {
        WorkoutSession session = WorkoutSession.of(EntityId.generate(), EntityId.generate(), startedAt,
                Map.of("note", "good"), List.of(WorkoutSessionEntry.ofRepetitions(1, 1, 1, 5)));

        assertThrows(UnsupportedOperationException.class,
                () -> session.getEntries().add(WorkoutSessionEntry.ofRepetitions(1, 1, 2, 5)));
        assertThrows(UnsupportedOperationException.class, () -> session.getMetadata().put("other", "value"));
    }

    @Test
    public void sessionsWithSameId_areEqual() {
        EntityId id = EntityId.generate();
        EntityId routineId = EntityId.generate();
        List<WorkoutSessionEntry> entries = List.of(WorkoutSessionEntry.ofRepetitions(1, 1, 1, 5));

        WorkoutSession first = WorkoutSession.of(id, routineId, startedAt, Map.of(), entries);
        WorkoutSession second = WorkoutSession.of(id, routineId, startedAt.plusDays(1), Map.of(), entries);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first, WorkoutSession.of(EntityId.generate(), routineId, startedAt, Map.of(), entries));
    }
}
