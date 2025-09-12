package com.phips30.workouttracker.workout.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static com.phips30.workouttracker.RandomData.randomString;
import static com.phips30.workouttracker.RandomData.shortString;
import static org.junit.jupiter.api.Assertions.*;

class ExerciseNameTest {

    @Test
    public void initExerciseName_shortName_valid() {
        String name = randomString(3);
        ExerciseName exerciseName = new ExerciseName(name);
        assertEquals(name, exerciseName.getValue());
    }

    @Test
    public void initExerciseName_longName_valid() {
        String name = randomString(20);
        ExerciseName exerciseName = new ExerciseName(name);
        assertEquals(name, exerciseName.getValue());
    }

    @Test
    public void initExerciseName_nameLessThan3Chars_throwsException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ExerciseName(randomString(2)));
        assertEquals("ExerciseName cannot be less than three characters", exception.getMessage());
    }

    @Test
    public void initExerciseName_nameLessThan3CharsButSpaces_throwsException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ExerciseName(" " + randomString(2) + " "));
        assertEquals("ExerciseName cannot be less than three characters", exception.getMessage());
    }

    @Test
    public void initExerciseName_emptyName_throwsException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ExerciseName(""));
        assertEquals("ExerciseName cannot be null or empty", exception.getMessage());
    }

    @Test
    public void initExerciseName_nullName_throwsException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ExerciseName(null));
        assertEquals("ExerciseName cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testEqualsAndHashCode() {
        String name = shortString();
        ExerciseName name1 = new ExerciseName(name);
        ExerciseName name2 = new ExerciseName(name);
        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
    }
}