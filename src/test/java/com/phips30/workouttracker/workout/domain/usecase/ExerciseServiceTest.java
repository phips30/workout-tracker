package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {
    private final String exerciseName = RandomData.shortString();

    @InjectMocks
    private ExerciseService exerciseService;
    @Mock
    private ExerciseRepository exerciseRepository;

    @Test
    public void createExercise_alreadyExists_throwsError() throws ExerciseAlreadyExistsException {
        try {
            when(exerciseRepository.exists(exerciseName)).thenReturn(true);
            exerciseService.create(exerciseName);
            fail("Expected Exception");
        } catch (Exception e) {
            assertEquals(String.format("Exercise %s already exists", exerciseName), e.getMessage());
        }
    }

    @Test
    public void createExercise_noName_throwsError() throws ExerciseAlreadyExistsException {
        try {
            exerciseService.create(null);
            fail("Expected Exception");
        } catch (Exception e) {
            assertEquals("ExerciseName cannot be null or empty", e.getMessage());
        }
    }

    @Test
    public void createExercise_emptyName_throwsError() throws ExerciseAlreadyExistsException {
        try {
            exerciseService.create("");
            fail("Expected Exception");
        } catch (Exception e) {
            assertEquals("ExerciseName cannot be null or empty", e.getMessage());
        }
    }

    @Test
    public void createExercise_validNameAndDoesNotExist_returnsExerciseObject() throws ExerciseAlreadyExistsException {
        Exercise exercise = exerciseService.create(exerciseName);
        assertEquals(exercise.getName().getValue(), exerciseName);
    }
}