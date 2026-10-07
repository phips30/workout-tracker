package com.phips30.workouttracker.workout.application.usecase;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.TestDataGenerator.RoutineFactory;
import com.phips30.workouttracker.workout.application.command.CreateRoutineCommand;
import com.phips30.workouttracker.workout.application.result.RoutineDetailResult;
import com.phips30.workouttracker.workout.application.result.RoutineResult;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseNotFoundException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.repository.ExerciseRepository;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoutineServiceTest {

    @Mock
    private RoutineRepository routineRepository;
    @Mock
    private ExerciseRepository exerciseRepository;

    private RoutineService routineService;

    private final String routineName = RandomData.shortString();

    private final Exercise exercise1 = new Exercise(new ExerciseName(RandomData.shortString()));
    private final Exercise exercise2 = new Exercise(new ExerciseName(RandomData.shortString()));

    @BeforeEach
    void setUp() {
        routineService = new RoutineService(
                routineRepository,
                new com.phips30.workouttracker.workout.domain.service.RoutineFactory(routineRepository, exerciseRepository));
    }

    private CreateRoutineCommand createCommand() {
        return new CreateRoutineCommand(
                routineName,
                RoutineType.AMRAP.name(),
                List.of(exercise1.getId().getId(), exercise2.getId().getId()),
                List.of(5, 10));
    }

    private List<EntityId> entityIds(CreateRoutineCommand command) {
        return command.exerciseIds().stream().map(EntityId::new).toList();
    }

    @Test
    public void createRoutine_doesNotExist_savesRoutine() throws RoutineAlreadyExistsException, ExerciseNotFoundException {
        CreateRoutineCommand command = createCommand();
        when(exerciseRepository.loadByIds(entityIds(command))).thenReturn(List.of(exercise1, exercise2));
        when(routineRepository.exists(new RoutineName(routineName))).thenReturn(false);

        routineService.createRoutine(command);

        ArgumentCaptor<Routine> captor = ArgumentCaptor.forClass(Routine.class);
        verify(routineRepository).saveRoutine(captor.capture());
        Routine saved = captor.getValue();
        assertEquals(routineName, saved.getName().getValue());
        assertEquals(RoutineType.AMRAP, saved.getRoutineType());
        assertEquals(List.of(exercise1, exercise2), saved.getExercises());
        assertEquals(List.of(5, 10), saved.getRepetitions().stream().map(r -> r.getNumber()).toList());
    }

    @Test
    public void createRoutine_alreadyExists_throwsErrorAndDoesNotSave() {
        CreateRoutineCommand command = createCommand();
        when(routineRepository.exists(new RoutineName(routineName))).thenReturn(true);

        RoutineAlreadyExistsException exception =
                assertThrows(RoutineAlreadyExistsException.class, () -> routineService.createRoutine(command));

        assertEquals(String.format("Routine %s already exists", routineName), exception.getMessage());
        verify(routineRepository, never()).saveRoutine(any());
    }

    @Test
    public void createRoutine_exerciseDoesNotExist_throwsErrorAndDoesNotSave() {
        CreateRoutineCommand command = createCommand();
        when(routineRepository.exists(new RoutineName(routineName))).thenReturn(false);
        when(exerciseRepository.loadByIds(entityIds(command))).thenReturn(List.of(exercise1));

        assertThrows(ExerciseNotFoundException.class, () -> routineService.createRoutine(command));
        verify(routineRepository, never()).saveRoutine(any());
    }

    @Test
    public void createRoutine_unknownRoutineType_throwsError() {
        CreateRoutineCommand command = new CreateRoutineCommand(
                routineName, "UNKNOWN", List.of(exercise1.getId().getId()), List.of(5));

        assertThrows(IllegalArgumentException.class, () -> routineService.createRoutine(command));
        verify(routineRepository, never()).saveRoutine(any());
    }

    @Test
    public void loadRoutines_routinesExist_returnsRoutineResults() {
        Routine first = RoutineFactory.createRoutine().build();
        Routine second = RoutineFactory.createRoutine().build();
        when(routineRepository.loadRoutines()).thenReturn(List.of(first, second));

        List<RoutineResult> routines = routineService.loadRoutines();

        assertEquals(List.of(
                new RoutineResult(first.getName().getValue(), first.getRoutineType().name()),
                new RoutineResult(second.getName().getValue(), second.getRoutineType().name())),
                routines);
    }

    @Test
    public void loadRoutine_exists_returnsRoutineDetailResult() throws RoutineNotFoundException {
        Routine routine = RoutineFactory.createRoutine(routineName).build();
        when(routineRepository.loadRoutine(new RoutineName(routineName))).thenReturn(Optional.of(routine));

        RoutineDetailResult details = routineService.loadRoutine(routineName);

        assertEquals(2, details.exercises().size());
        assertEquals(routine.getExercises().getFirst().getId().getId().toString(), details.exercises().getFirst().id());
        assertEquals(routine.getExercises().getFirst().getName().getValue(), details.exercises().getFirst().name());
        assertEquals(2, details.repetitions().size());
        assertEquals(routine.getRepetitions().getFirst().getNumber(), details.repetitions().getFirst().number());
        assertEquals(routine.getRepetitions().getFirst().getType().name(), details.repetitions().getFirst().type());
    }

    @Test
    public void loadRoutine_doesNotExist_throwsNotFound() {
        when(routineRepository.loadRoutine(new RoutineName(routineName))).thenReturn(Optional.empty());

        RoutineNotFoundException exception =
                assertThrows(RoutineNotFoundException.class, () -> routineService.loadRoutine(routineName));

        assertEquals(String.format("Routine %s does not exist", routineName), exception.getMessage());
    }
}
