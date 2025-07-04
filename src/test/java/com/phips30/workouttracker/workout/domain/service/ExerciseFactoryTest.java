package com.phips30.workouttracker.workout.domain.service;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseFactoryTest {
    private final String exerciseName = RandomData.shortString();

    @InjectMocks
    private ExerciseFactory exerciseFactory;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Test
    public void createExercise_alreadyExists_throwsError() {
        try {
            when(exerciseRepository.exists(exerciseName)).thenReturn(true);
            exerciseFactory.of(exerciseName);
            fail("Expected Exception");
        } catch (Exception e) {
            assertEquals(String.format("Exercise %s already exists", exerciseName), e.getMessage());
        }
    }

    @Test
    public void createExercise_noName_throwsError() {
        try {
            exerciseFactory.of(null);
            fail("Expected Exception");
        } catch (Exception e) {
            assertEquals("Name is null or empty", e.getMessage());
        }
    }

    @Test
    public void createExercise_emptyName_throwsError() {
        try {
            exerciseFactory.of("");
            fail("Expected Exception");
        } catch (Exception e) {
            assertEquals("Name is null or empty", e.getMessage());
        }
    }

    @Test
    public void createExercise_validNameAndDoesNotExist_returnsExerciseObject() throws ExerciseAlreadyExistsException {
        Exercise exercise = exerciseFactory.of(exerciseName);
        assertEquals(exercise.getName(), exerciseName);
    }
}