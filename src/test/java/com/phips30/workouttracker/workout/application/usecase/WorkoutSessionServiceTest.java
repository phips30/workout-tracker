package com.phips30.workouttracker.workout.application.usecase;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.TestDataGenerator.RoutineFactory;
import com.phips30.workouttracker.workout.application.command.CreateWorkoutSessionCommand;
import com.phips30.workouttracker.workout.application.result.WorkoutSessionResult;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.WorkoutSession;
import com.phips30.workouttracker.workout.domain.entity.WorkoutSessionEntry;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.repository.WorkoutSessionRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkoutSessionServiceTest {

    @InjectMocks
    private WorkoutSessionService workoutSessionService;
    @Mock
    private RoutineRepository routineRepository;
    @Mock
    private WorkoutSessionRepository workoutSessionRepository;

    private final String routineName = RandomData.shortString();
    private final LocalDateTime startedAt = LocalDateTime.now();

    // the generated routine has item 1 in repetitions and item 2 in seconds in block 1
    private CreateWorkoutSessionCommand createCommand(List<CreateWorkoutSessionCommand.Entry> entries) {
        return new CreateWorkoutSessionCommand(routineName, startedAt, Map.of("weight", 10), entries);
    }

    @Test
    public void saveWorkoutSession_routineExists_savesSessionForRoutine() throws RoutineNotFoundException {
        Routine routine = RoutineFactory.createRoutine(routineName).build();
        when(routineRepository.loadRoutine(new RoutineName(routineName))).thenReturn(Optional.of(routine));

        workoutSessionService.saveWorkoutSession(createCommand(List.of(
                new CreateWorkoutSessionCommand.Entry(1, 1, 1, 7, false),
                new CreateWorkoutSessionCommand.Entry(1, 1, 2, 0, true))));

        ArgumentCaptor<WorkoutSession> captor = ArgumentCaptor.forClass(WorkoutSession.class);
        verify(workoutSessionRepository).save(captor.capture());
        WorkoutSession saved = captor.getValue();
        assertEquals(routine.getId(), saved.getRoutineId());
        assertEquals(startedAt, saved.getStartedAt());
        assertEquals(Map.of("weight", 10), saved.getMetadata());
        assertEquals(List.of(
                WorkoutSessionEntry.ofRepetitions(1, 1, 1, 7),
                WorkoutSessionEntry.ofSeconds(1, 1, 2, true)), saved.getEntries());
    }

    @Test
    public void saveWorkoutSession_routineDoesNotExist_throwsNotFound() {
        when(routineRepository.loadRoutine(new RoutineName(routineName))).thenReturn(Optional.empty());

        RoutineNotFoundException exception = assertThrows(RoutineNotFoundException.class,
                () -> workoutSessionService.saveWorkoutSession(createCommand(List.of())));

        assertEquals(String.format("Routine %s does not exist", routineName), exception.getMessage());
        verify(workoutSessionRepository, never()).save(any());
    }

    @Test
    public void saveWorkoutSession_noEntries_throwsError() {
        Routine routine = RoutineFactory.createRoutine(routineName).build();
        when(routineRepository.loadRoutine(new RoutineName(routineName))).thenReturn(Optional.of(routine));

        assertThrows(IllegalArgumentException.class,
                () -> workoutSessionService.saveWorkoutSession(createCommand(List.of())));
        verify(workoutSessionRepository, never()).save(any());
    }

    @Test
    public void saveWorkoutSession_entryReferencesUnknownItem_throwsError() {
        Routine routine = RoutineFactory.createRoutine(routineName).build();
        when(routineRepository.loadRoutine(new RoutineName(routineName))).thenReturn(Optional.of(routine));

        assertThrows(IllegalArgumentException.class, () -> workoutSessionService.saveWorkoutSession(
                createCommand(List.of(new CreateWorkoutSessionCommand.Entry(1, 1, 99, 1, false)))));
        verify(workoutSessionRepository, never()).save(any());
    }

    @Test
    public void loadWorkoutSessionsForRoutine_returnsSessionsOldestFirst() throws RoutineNotFoundException {
        Routine routine = RoutineFactory.createRoutine(routineName).build();
        when(routineRepository.loadRoutine(new RoutineName(routineName))).thenReturn(Optional.of(routine));
        List<WorkoutSessionEntry> entries = List.of(WorkoutSessionEntry.ofRepetitions(1, 1, 1, 5));
        WorkoutSession newer = WorkoutSession.of(EntityId.generate(), routine.getId(), startedAt, Map.of(), entries);
        WorkoutSession older = WorkoutSession.of(
                EntityId.generate(), routine.getId(), startedAt.minusDays(1), Map.of(), entries);
        when(workoutSessionRepository.loadForRoutine(routine.getId())).thenReturn(List.of(newer, older));

        List<WorkoutSessionResult> results = workoutSessionService.loadWorkoutSessionsForRoutine(routineName);

        assertEquals(List.of(older.getId().toString(), newer.getId().toString()),
                results.stream().map(WorkoutSessionResult::id).toList());
        assertEquals(1, results.getFirst().entries().size());
        assertEquals(5, results.getFirst().entries().getFirst().completedRepetitions());
        assertNull(results.getFirst().entries().getFirst().completed());
    }

    @Test
    public void loadWorkoutSessionsForRoutine_routineDoesNotExist_throwsNotFound() {
        when(routineRepository.loadRoutine(new RoutineName(routineName))).thenReturn(Optional.empty());

        assertThrows(RoutineNotFoundException.class,
                () -> workoutSessionService.loadWorkoutSessionsForRoutine(routineName));
    }
}
