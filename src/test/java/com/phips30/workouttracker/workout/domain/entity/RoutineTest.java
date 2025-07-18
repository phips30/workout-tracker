package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoutineTest {

    private final String routineName = "CVP";
    private final RoutineType routineType = RoutineType.AMRAP;
    private final List<Exercise> exercises = List.of(
            Exercise.of("Burpees"),
            Exercise.of("Mountain climbers"),
            Exercise.of("4-Count burpees"),
            Exercise.of("Jumping jacks")
    );
    private final List<Repetition> repetitions = List.of(
            Repetition.of(10),
            Repetition.of(40),
            Repetition.of(10),
            Repetition.of(10)
    );

    @Test
    public void initRoutineWithProperValues() {
        Routine firstAmrapRoutine = Routine.of(
                routineName,
                routineType,
                exercises,
                repetitions
        );

        assertEquals(routineName, firstAmrapRoutine.getName());
        assertEquals(exercises.size(), firstAmrapRoutine.getExercises().size());
        assertEquals(repetitions.size(), firstAmrapRoutine.getRepetitions().size());
        assertEquals(0, firstAmrapRoutine.getWorkouts().size());
    }

    @Test
    public void initRoutineWithNoName_throwsException() {
        try {
            Routine.of(
                    null,
                    routineType,
                    List.of(),
                    List.of()
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Routine name is null or empty", e.getMessage());
        }
    }

    @Test
    public void initRoutineWithEmptyName_throwsException() {
        try {
            Routine.of(
                    null,
                    routineType,
                    List.of(),
                    List.of()
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Routine name is null or empty", e.getMessage());
        }
    }

    @Test
    public void initRoutineWithNoRoutineType_throwsException() {
        try {
            Routine.of(
                    routineName,
                    null,
                    List.of(),
                    List.of()
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("RoutineType is null", e.getMessage());
        }
    }

    @Test
    public void initRoutineWithNoExercises_throwsException() {
        try {
            Routine.of(
                    routineName,
                    routineType,
                    List.of(),
                    List.of()
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Exercises is null or empty", e.getMessage());
        }
    }

    @Test
    public void initRoutineWithNoRepetitions_throwsException() {
        try {
            Routine.of(
                    routineName,
                    routineType,
                    exercises,
                    List.of()
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Repetitions is null or empty", e.getMessage());
        }
    }

    @Test
    public void initRoutineWithNotMatchingExercisesAndRepetitions_throwsException() {
        try {
            Routine.of(
                    routineName,
                    routineType,
                    exercises,
                    List.of(Repetition.of(10))
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Each exercise must have a corresponding repetition", e.getMessage());
        }
    }
}