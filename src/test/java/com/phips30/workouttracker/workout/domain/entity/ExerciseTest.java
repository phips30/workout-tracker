package com.phips30.workouttracker.workout.domain.entity;

import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import org.junit.jupiter.api.Test;

import static com.phips30.workouttracker.RandomData.randomString;
import static org.junit.jupiter.api.Assertions.*;

class ExerciseTest {

    EntityId entityId = EntityId.generate();
    ExerciseName exerciseName = new ExerciseName(randomString(5));

    @Test
    public void initNewExercise_allValuesProvided_valid() {
        Exercise exercise = new Exercise(exerciseName);
        assertNotNull(exercise.getId().getId());
        assertEquals(exerciseName.getValue(), exercise.getName().getValue());
    }

    @Test
    public void initExistingExercise_allValuesProvided_valid() {
        Exercise exercise = new Exercise(entityId, exerciseName);
        assertEquals(entityId.getId(), exercise.getId().getId());
        assertEquals(exerciseName.getValue(), exercise.getName().getValue());
    }

    @Test
    public void initNewExercise_noName_throwsException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Exercise(null));
        assertEquals("ExerciseName is null", exception.getMessage());
    }

    @Test
    public void initExistingExercise_noId_throwsException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Exercise(null, exerciseName));
        assertEquals("EntityId is null", exception.getMessage());
    }

    @Test
    public void initExistingExercise_noName_throwsException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Exercise(entityId, null));
        assertEquals("ExerciseName is null", exception.getMessage());
    }

    @Test
    public void testEquals_sameObject_isEqual() {
        Exercise exercise = new Exercise(exerciseName);
        assertEquals(exercise, exercise);
        assertEquals(exercise.hashCode(), exercise.hashCode());
    }

    @Test
    public void testEquals_differentObject_isNotEqual() {
        Exercise exercise1 = new Exercise(exerciseName);
        Exercise exercise2 = new Exercise(exerciseName);
        assertNotEquals(exercise1, exercise2);
        assertNotEquals(exercise1.hashCode(), exercise2.hashCode());
    }

    @Test
    public void testToString() {
        Exercise exercise = new Exercise(entityId, exerciseName);
        String expected = String.format("Exercise{id=%s, name=%s}", entityId, exerciseName);
        assertEquals(expected, exercise.toString());
    }
}