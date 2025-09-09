package com.phips30.workouttracker.workout.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static com.phips30.workouttracker.RandomData.shortString;
import static org.junit.jupiter.api.Assertions.*;

class RoutineNameTest {

    @Test
    public void initRoutineName_valid() {
        String name = shortString();
        RoutineName routineName = new RoutineName(name);
        assertEquals(name, routineName.getValue());
    }

    @Test
    public void initRoutineName_emptyName_throwsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RoutineName("");
        });
        assertEquals("Routine name cannot be null or empty", exception.getMessage());
    }

    @Test
    public void initRoutineName_nullName_throwsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RoutineName(null);
        });
        assertEquals("Routine name cannot be null or empty", exception.getMessage());
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