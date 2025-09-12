package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoutineTest {

    private final String routineName = "CVP";
    private final RoutineType routineType = RoutineType.AMRAP;
    private final List<Exercise> exercises = List.of(
            new Exercise(new ExerciseName("Burpees")),
            new Exercise(new ExerciseName("Mountain climbers")),
            new Exercise(new ExerciseName("4-Count burpees")),
            new Exercise(new ExerciseName("Jumping jacks"))
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
                EntityId.generate(),
                routineName,
                routineType,
                exercises,
                repetitions
        );

        assertEquals(routineName, firstAmrapRoutine.getName().getValue());
        assertEquals(exercises.size(), firstAmrapRoutine.getExercises().size());
        assertEquals(repetitions.size(), firstAmrapRoutine.getRepetitions().size());
    }

    @Test
    public void initRoutineWithNoName_throwsException() {
        try {
            Routine.of(
                    EntityId.generate(),
                    null,
                    routineType,
                    List.of(),
                    List.of()
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("RoutineName cannot be null or empty", e.getMessage());
        }
    }

    @Test
    public void initRoutineWithEmptyName_throwsException() {
        try {
            Routine.of(
                    EntityId.generate(),
                    null,
                    routineType,
                    List.of(),
                    List.of()
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("RoutineName cannot be null or empty", e.getMessage());
        }
    }

    @Test
    public void initRoutineWithNoRoutineType_throwsException() {
        try {
            Routine.of(
                    EntityId.generate(),
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
                    EntityId.generate(),
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
                    EntityId.generate(),
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
                    EntityId.generate(),
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