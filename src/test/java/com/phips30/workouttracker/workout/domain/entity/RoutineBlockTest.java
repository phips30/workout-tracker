package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RepetitionType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoutineBlockTest {

    private final Exercise burpees = new Exercise(new ExerciseName("Burpees"));
    private final Exercise plank = new Exercise(new ExerciseName("Plank"));

    private final RoutineBlockItem first = RoutineBlockItem.of(1, burpees, Repetition.of(10));
    private final RoutineBlockItem second = RoutineBlockItem.of(2, plank, Repetition.of(RepetitionType.SECONDS, 30));

    @Test
    public void initBlockWithProperValues() {
        RoutineBlock block = RoutineBlock.of(1, 3, List.of(first, second));

        assertEquals(1, block.getPosition());
        assertEquals(3, block.getRounds());
        assertEquals(List.of(first, second), block.getItems());
    }

    @Test
    public void initBlockWithItemsOutOfOrder_sortsItemsByPosition() {
        RoutineBlock block = RoutineBlock.of(1, 3, List.of(second, first));

        assertEquals(List.of(first, second), block.getItems());
    }

    @Test
    public void initBlockWithInvalidPosition_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> RoutineBlock.of(0, 3, List.of(first)));
    }

    @Test
    public void initBlockWithInvalidRounds_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> RoutineBlock.of(1, 0, List.of(first)));
    }

    @Test
    public void initBlockWithNoItems_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                RoutineBlock.of(1, 3, List.of()));
        assertEquals("Items is null or empty", e.getMessage());
    }

    @Test
    public void initBlockWithDuplicateItemPositions_throwsException() {
        RoutineBlockItem duplicate = RoutineBlockItem.of(1, plank, Repetition.of(5));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                RoutineBlock.of(1, 3, List.of(first, duplicate)));
        assertEquals("Item positions must be unique and start at 1 without gaps", e.getMessage());
    }

    @Test
    public void initBlockWithGapInItemPositions_throwsException() {
        RoutineBlockItem third = RoutineBlockItem.of(3, plank, Repetition.of(5));

        assertThrows(IllegalArgumentException.class, () -> RoutineBlock.of(1, 3, List.of(first, third)));
    }

    @Test
    public void getItems_isNotModifiable() {
        RoutineBlock block = RoutineBlock.of(1, 3, List.of(first));

        assertThrows(UnsupportedOperationException.class, () -> block.getItems().add(second));
    }

    @Test
    public void blocksWithSameValuesAreEqual() {
        assertEquals(RoutineBlock.of(1, 3, List.of(first)), RoutineBlock.of(1, 3, List.of(first)));
        assertNotEquals(RoutineBlock.of(1, 3, List.of(first)), RoutineBlock.of(1, 4, List.of(first)));
    }
}
