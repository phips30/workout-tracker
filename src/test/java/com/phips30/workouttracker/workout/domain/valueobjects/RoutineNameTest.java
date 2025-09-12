package com.phips30.workouttracker.workout.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static com.phips30.workouttracker.RandomData.randomString;
import static com.phips30.workouttracker.RandomData.shortString;
import static org.junit.jupiter.api.Assertions.*;

class RoutineNameTest {

    @Test
    public void initRoutineName_shortName_valid() {
        String name = randomString(3);
        RoutineName routineName = new RoutineName(name);
        assertEquals(name, routineName.getValue());
    }

    @Test
    public void initRoutineName_longName_valid() {
        String name = randomString(20);
        RoutineName routineName = new RoutineName(name);
        assertEquals(name, routineName.getValue());
    }

    @Test
    public void initRoutineName_nameLessThan3Chars_throwsException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new RoutineName(randomString(2)));
        assertEquals("RoutineName cannot be less than three characters", exception.getMessage());
    }

    @Test
    public void initRoutineName_nameLessThan3CharsButSpaces_throwsException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new RoutineName(" " + randomString(2) + " "));
        assertEquals("RoutineName cannot be less than three characters", exception.getMessage());
    }

    @Test
    public void initRoutineName_emptyName_throwsException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new RoutineName(""));
        assertEquals("RoutineName cannot be null or empty", exception.getMessage());
    }

    @Test
    public void initRoutineName_nullName_throwsException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new RoutineName(null));
        assertEquals("RoutineName cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testEqualsAndHashCode() {
        String name = shortString();
        RoutineName name1 = new RoutineName(name);
        RoutineName name2 = new RoutineName(name);
        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
    }
}