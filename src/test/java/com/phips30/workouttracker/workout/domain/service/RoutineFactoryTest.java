package com.phips30.workouttracker.workout.domain.service;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineBlock;
import com.phips30.workouttracker.workout.domain.entity.RoutineBlockItem;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseNotFoundException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.service.RoutineFactory.BlockDefinition;
import com.phips30.workouttracker.workout.domain.service.RoutineFactory.ItemDefinition;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RepetitionType;
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

    private List<BlockDefinition> twoItemBlock() {
        return List.of(new BlockDefinition(1, 3, List.of(
                new ItemDefinition(1, exercise1.getId(), Repetition.of(5)),
                new ItemDefinition(2, exercise2.getId(), Repetition.of(RepetitionType.SECONDS, 30)))));
    }

    @Test
    public void of_routineAlreadyExists_throwsError() {
        when(routineRepository.exists(routineName)).thenReturn(true);

        RoutineAlreadyExistsException exception = assertThrows(RoutineAlreadyExistsException.class, () ->
                routineFactory.of(routineName, RoutineType.AMRAP, twoItemBlock()));

        assertEquals(String.format("Routine %s already exists", routineName.getValue()), exception.getMessage());
        verifyNoInteractions(exerciseRepository);
    }

    @Test
    public void of_exerciseDoesNotExist_throwsError() {
        when(routineRepository.exists(routineName)).thenReturn(false);
        when(exerciseRepository.loadByIds(List.of(exercise1.getId(), exercise2.getId()))).thenReturn(List.of(exercise1));

        ExerciseNotFoundException exception = assertThrows(ExerciseNotFoundException.class, () ->
                routineFactory.of(routineName, RoutineType.AMRAP, twoItemBlock()));

        assertEquals(String.format("Exercise %s does not exist", exercise2.getId()), exception.getMessage());
    }

    @Test
    public void of_noBlocks_throwsError() {
        when(routineRepository.exists(routineName)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                routineFactory.of(routineName, RoutineType.AMRAP, List.of()));
        verifyNoInteractions(exerciseRepository);
    }

    @Test
    public void of_validInput_returnsRoutineWithBlocksAndItems()
            throws RoutineAlreadyExistsException, ExerciseNotFoundException {
        when(routineRepository.exists(routineName)).thenReturn(false);
        // the repository returns the exercises in a different order than requested
        when(exerciseRepository.loadByIds(List.of(exercise1.getId(), exercise2.getId())))
                .thenReturn(List.of(exercise2, exercise1));

        Routine routine = routineFactory.of(routineName, RoutineType.AMRAP, twoItemBlock());

        assertEquals(routineName, routine.getName());
        assertEquals(RoutineType.AMRAP, routine.getRoutineType());
        assertEquals(List.of(RoutineBlock.of(1, 3, List.of(
                RoutineBlockItem.of(1, exercise1, Repetition.of(5)),
                RoutineBlockItem.of(2, exercise2, Repetition.of(RepetitionType.SECONDS, 30))))),
                routine.getBlocks());
    }

    @Test
    public void of_sameExerciseInSeveralBlocks_loadsExerciseOnce() throws RoutineAlreadyExistsException, ExerciseNotFoundException {
        List<BlockDefinition> blocks = List.of(
                new BlockDefinition(1, 2, List.of(new ItemDefinition(1, exercise1.getId(), Repetition.of(5)))),
                new BlockDefinition(2, 4, List.of(
                        new ItemDefinition(1, exercise1.getId(), Repetition.of(10)),
                        new ItemDefinition(2, exercise1.getId(), Repetition.of(15)))));
        when(routineRepository.exists(routineName)).thenReturn(false);
        when(exerciseRepository.loadByIds(List.of(exercise1.getId()))).thenReturn(List.of(exercise1));

        Routine routine = routineFactory.of(routineName, RoutineType.AMRAP, blocks);

        assertEquals(2, routine.getBlocks().size());
        assertEquals(exercise1, routine.getBlocks().get(1).getItems().get(1).getExercise());
    }

    @Test
    public void of_invalidPositions_throwsError() {
        List<BlockDefinition> blocks = List.of(new BlockDefinition(1, 3, List.of(
                new ItemDefinition(2, exercise1.getId(), Repetition.of(5)))));
        when(routineRepository.exists(routineName)).thenReturn(false);
        when(exerciseRepository.loadByIds(List.of(exercise1.getId()))).thenReturn(List.of(exercise1));

        assertThrows(IllegalArgumentException.class, () -> routineFactory.of(routineName, RoutineType.AMRAP, blocks));
    }
}
