package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoutineTest {

    private final RoutineName routineName = new RoutineName("CVP");
    private final RoutineType routineType = RoutineType.AMRAP;
    private final Exercise burpees = new Exercise(new ExerciseName("Burpees"));
    private final Exercise mountainClimbers = new Exercise(new ExerciseName("Mountain climbers"));

    private RoutineBlock block(int position) {
        return RoutineBlock.of(position, 3, List.of(
                RoutineBlockItem.of(1, burpees, Repetition.of(10)),
                RoutineBlockItem.of(2, mountainClimbers, Repetition.of(40))));
    }

    @Test
    public void initRoutineWithProperValues() {
        Routine routine = Routine.of(EntityId.generate(), routineName, routineType, List.of(block(1), block(2)));

        assertEquals(routineName, routine.getName());
        assertEquals(routineType, routine.getRoutineType());
        assertEquals(List.of(block(1), block(2)), routine.getBlocks());
    }

    @Test
    public void initRoutineWithBlocksOutOfOrder_sortsBlocksByPosition() {
        Routine routine = Routine.of(EntityId.generate(), routineName, routineType, List.of(block(2), block(1)));

        assertEquals(List.of(1, 2), routine.getBlocks().stream().map(RoutineBlock::getPosition).toList());
    }

    @Test
    public void createNew_generatesId() {
        Routine routine = Routine.createNew(routineName, routineType, List.of(block(1)));

        assertNotNull(routine.getId());
    }

    @Test
    public void getBlocks_isNotModifiable() {
        Routine routine = Routine.of(EntityId.generate(), routineName, routineType, List.of(block(1)));

        assertThrows(UnsupportedOperationException.class, () -> routine.getBlocks().add(block(2)));
    }

    @Test
    public void initRoutineWithNoName_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                Routine.of(EntityId.generate(), null, routineType, List.of(block(1))));
        assertEquals("RoutineName id is null", e.getMessage());
    }

    @Test
    public void initRoutineWithNoRoutineType_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                Routine.of(EntityId.generate(), routineName, null, List.of(block(1))));
        assertEquals("RoutineType is null", e.getMessage());
    }

    @Test
    public void initRoutineWithNoBlocks_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                Routine.of(EntityId.generate(), routineName, routineType, List.of()));
        assertEquals("Blocks is null or empty", e.getMessage());
    }

    @Test
    public void initRoutineWithDuplicateBlockPositions_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                Routine.of(EntityId.generate(), routineName, routineType, List.of(block(1), block(1))));
        assertEquals("Block positions must be unique and start at 1 without gaps", e.getMessage());
    }

    @Test
    public void initRoutineWithGapInBlockPositions_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                Routine.of(EntityId.generate(), routineName, routineType, List.of(block(1), block(3))));
        assertEquals("Block positions must be unique and start at 1 without gaps", e.getMessage());
    }
}
