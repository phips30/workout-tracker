package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RepetitionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoutineBlockItemTest {

    private final Exercise exercise = new Exercise(new ExerciseName("Plank"));
    private final Repetition repetition = Repetition.of(RepetitionType.SECONDS, 30);

    @Test
    public void initItemWithProperValues() {
        RoutineBlockItem item = RoutineBlockItem.of(2, exercise, repetition);

        assertEquals(2, item.getPosition());
        assertEquals(exercise, item.getExercise());
        assertEquals(repetition, item.getRepetition());
    }

    @Test
    public void initItemWithInvalidPosition_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> RoutineBlockItem.of(0, exercise, repetition));
    }

    @Test
    public void initItemWithNoExercise_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                RoutineBlockItem.of(1, null, repetition));
        assertEquals("Exercise is null", e.getMessage());
    }

    @Test
    public void initItemWithNoRepetition_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                RoutineBlockItem.of(1, exercise, null));
        assertEquals("Repetition is null", e.getMessage());
    }

    @Test
    public void itemsWithSameValuesAreEqual() {
        assertEquals(RoutineBlockItem.of(1, exercise, repetition), RoutineBlockItem.of(1, exercise, repetition));
        assertNotEquals(RoutineBlockItem.of(1, exercise, repetition), RoutineBlockItem.of(2, exercise, repetition));
    }
}
