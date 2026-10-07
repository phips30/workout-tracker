package com.phips30.workouttracker.workout.domain.service;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseNotFoundException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoutineFactoryTest {

    private final RoutineName routineName = new RoutineName(RandomData.shortString());
    private final Exercise exercise1 = new Exercise(new ExerciseName(RandomData.shortString()));
    private final Exercise exercise2 = new Exercise(new ExerciseName(RandomData.shortString()));

    @InjectMocks
    private RoutineFactory routineFactory;
    @Mock
    private RoutineRepository routineRepository;
    @Mock
    private ExerciseRepository exerciseRepository;

    @Test
    public void of_routineAlreadyExists_throwsError() {
        when(routineRepository.exists(routineName)).thenReturn(true);

        RoutineAlreadyExistsException exception = assertThrows(RoutineAlreadyExistsException.class, () ->
                routineFactory.of(routineName, RoutineType.AMRAP, List.of(exercise1.getId()), List.of(Repetition.of(5))));

        assertEquals(String.format("Routine %s already exists", routineName.getValue()), exception.getMessage());
        verifyNoInteractions(exerciseRepository);
    }

    @Test
    public void of_exerciseDoesNotExist_throwsError() {
        List<EntityId> exerciseIds = List.of(exercise1.getId(), exercise2.getId());
        when(routineRepository.exists(routineName)).thenReturn(false);
        when(exerciseRepository.loadByIds(exerciseIds)).thenReturn(List.of(exercise1));

        ExerciseNotFoundException exception = assertThrows(ExerciseNotFoundException.class, () ->
                routineFactory.of(routineName, RoutineType.AMRAP, exerciseIds, List.of(Repetition.of(5), Repetition.of(10))));

        assertEquals(String.format("Exercise %s does not exist", exercise2.getId()), exception.getMessage());
    }

    @Test
    public void of_validInput_returnsRoutineWithExercisesInRequestedOrder()
            throws RoutineAlreadyExistsException, ExerciseNotFoundException {
        List<EntityId> exerciseIds = List.of(exercise1.getId(), exercise2.getId());
        when(routineRepository.exists(routineName)).thenReturn(false);
        // the repository returns the exercises in a different order than requested
        when(exerciseRepository.loadByIds(exerciseIds)).thenReturn(List.of(exercise2, exercise1));

        Routine routine = routineFactory.of(
                routineName, RoutineType.AMRAP, exerciseIds, List.of(Repetition.of(5), Repetition.of(10)));

        assertEquals(routineName, routine.getName());
        assertEquals(RoutineType.AMRAP, routine.getRoutineType());
        assertEquals(List.of(exercise1, exercise2), routine.getExercises());
        assertEquals(List.of(5, 10), routine.getRepetitions().stream().map(Repetition::getNumber).toList());
    }

    @Test
    public void of_sameExerciseTwice_keepsBothEntries() throws RoutineAlreadyExistsException, ExerciseNotFoundException {
        List<EntityId> exerciseIds = List.of(exercise1.getId(), exercise1.getId());
        when(routineRepository.exists(routineName)).thenReturn(false);
        when(exerciseRepository.loadByIds(exerciseIds)).thenReturn(List.of(exercise1));

        Routine routine = routineFactory.of(
                routineName, RoutineType.AMRAP, exerciseIds, List.of(Repetition.of(5), Repetition.of(10)));

        assertEquals(List.of(exercise1, exercise1), routine.getExercises());
    }
}
