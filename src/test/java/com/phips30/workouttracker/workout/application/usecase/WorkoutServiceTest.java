package com.phips30.workouttracker.workout.application.usecase;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.TestDataGenerator.RoutineFactory;
import com.phips30.workouttracker.workout.application.command.CreateWorkoutCommand;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.repository.WorkoutRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
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
class WorkoutServiceTest {

    @InjectMocks
    private WorkoutService workoutService;
    @Mock
    private RoutineRepository routineRepository;
    @Mock
    private WorkoutRepository workoutRepository;

    private final String routineName = RandomData.shortString();
    private final LocalDateTime startedAt = LocalDateTime.now();

    private CreateWorkoutCommand createCommand() {
        return new CreateWorkoutCommand(
                routineName,
                startedAt,
                List.of(Duration.ofMinutes(10), Duration.ofMinutes(5)),
                Map.of("note", "felt good"));
    }

    @Test
    public void saveWorkout_routineExists_savesWorkoutForRoutine() throws RoutineNotFoundException {
        Routine routine = RoutineFactory.createRoutine(routineName).build();
        when(routineRepository.loadRoutine(new RoutineName(routineName))).thenReturn(Optional.of(routine));

        workoutService.saveWorkout(createCommand());

        ArgumentCaptor<Workout> captor = ArgumentCaptor.forClass(Workout.class);
        verify(workoutRepository).save(org.mockito.ArgumentMatchers.eq(routine), captor.capture());
        Workout saved = captor.getValue();
        assertEquals(startedAt, saved.getStartedAt());
        assertEquals(startedAt.plusMinutes(15), saved.getCompletedAt());
        assertEquals(2, saved.getRounds().size());
        assertEquals(Map.of("note", "felt good"), saved.getMetadata());
    }

    @Test
    public void saveWorkout_routineDoesNotExist_throwsNotFound() {
        when(routineRepository.loadRoutine(new RoutineName(routineName))).thenReturn(Optional.empty());

        RoutineNotFoundException exception =
                assertThrows(RoutineNotFoundException.class, () -> workoutService.saveWorkout(createCommand()));

        assertEquals(String.format("Routine %s does not exist", routineName), exception.getMessage());
        verify(workoutRepository, never()).save(any(), any());
    }

    @Test
    public void saveWorkout_noRounds_throwsError() {
        Routine routine = RoutineFactory.createRoutine(routineName).build();
        when(routineRepository.loadRoutine(new RoutineName(routineName))).thenReturn(Optional.of(routine));
        CreateWorkoutCommand command = new CreateWorkoutCommand(routineName, startedAt, List.of(), Map.of());

        assertThrows(IllegalArgumentException.class, () -> workoutService.saveWorkout(command));
        verify(workoutRepository, never()).save(any(), any());
    }
}
