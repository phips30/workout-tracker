package com.phips30.workouttracker.workout.application.usecase;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.application.result.ExerciseResult;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
            when(exerciseRepository.exists(new ExerciseName(exerciseName))).thenReturn(true);
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
    public void createExercise_validNameAndDoesNotExist_returnsExerciseResult() throws ExerciseAlreadyExistsException {
        Exercise savedExercise = new Exercise(new ExerciseName(exerciseName));

        when(exerciseRepository.exists(new ExerciseName(exerciseName))).thenReturn(false);
        when(exerciseRepository.save(ArgumentMatchers.any(Exercise.class))).thenReturn(savedExercise);

        ExerciseResult exercise = exerciseService.create(exerciseName);
        assertEquals(exerciseName, exercise.name());
        assertEquals(savedExercise.getId().getId().toString(), exercise.id());
    }

    @Test
    public void loadAll_exercisesExist_returnsExerciseResults() {
        Exercise first = new Exercise(new ExerciseName(RandomData.shortString()));
        Exercise second = new Exercise(new ExerciseName(RandomData.shortString()));

        when(exerciseRepository.loadAll()).thenReturn(List.of(first, second));

        List<ExerciseResult> exercises = exerciseService.loadAll();
        assertEquals(List.of(
                new ExerciseResult(first.getId().getId().toString(), first.getName().getValue()),
                new ExerciseResult(second.getId().getId().toString(), second.getName().getValue())),
                exercises);
    }
}