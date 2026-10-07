package com.phips30.workouttracker.workout.domain.service;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseFactoryTest {

    private final ExerciseName exerciseName = new ExerciseName(RandomData.shortString());

    @InjectMocks
    private ExerciseFactory exerciseFactory;
    @Mock
    private ExerciseRepository exerciseRepository;

    @Test
    public void of_nameAlreadyExists_throwsError() {
        when(exerciseRepository.exists(exerciseName)).thenReturn(true);

        ExerciseAlreadyExistsException exception =
                assertThrows(ExerciseAlreadyExistsException.class, () -> exerciseFactory.of(exerciseName));

        assertEquals(String.format("Exercise %s already exists", exerciseName.getValue()), exception.getMessage());
    }

    @Test
    public void of_nameDoesNotExist_returnsNewExercise() throws ExerciseAlreadyExistsException {
        when(exerciseRepository.exists(exerciseName)).thenReturn(false);

        Exercise exercise = exerciseFactory.of(exerciseName);

        assertEquals(exerciseName, exercise.getName());
        assertNotNull(exercise.getId());
    }
}
