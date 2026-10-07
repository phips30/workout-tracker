package com.phips30.workouttracker.workout.application.usecase;

import com.phips30.workouttracker.workout.application.command.CreateWorkoutSessionCommand;
import com.phips30.workouttracker.workout.application.result.WorkoutSessionResult;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.WorkoutSession;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.repository.WorkoutSessionRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;

import java.util.Comparator;
import java.util.List;

public class WorkoutSessionService {

    private final RoutineRepository routineRepository;
    private final WorkoutSessionRepository workoutSessionRepository;

    public WorkoutSessionService(RoutineRepository routineRepository, WorkoutSessionRepository workoutSessionRepository) {
        this.routineRepository = routineRepository;
        this.workoutSessionRepository = workoutSessionRepository;
    }

    public void saveWorkoutSession(CreateWorkoutSessionCommand command) throws RoutineNotFoundException {
        Routine routine = loadRoutine(command.routineName());

        List<WorkoutSession.EntryDefinition> entries = command.entries() == null
                ? List.of()
                : command.entries().stream()
                .map(entry -> new WorkoutSession.EntryDefinition(
                        entry.blockPosition(),
                        entry.round(),
                        entry.itemPosition(),
                        entry.completedRepetitions(),
                        entry.completed()))
                .toList();

        workoutSessionRepository.save(
                WorkoutSession.createNew(routine, command.startedAt(), command.metadata(), entries));
    }

    public List<WorkoutSessionResult> loadWorkoutSessionsForRoutine(String routineName) throws RoutineNotFoundException {
        Routine routine = loadRoutine(routineName);
        return workoutSessionRepository.loadForRoutine(routine.getId()).stream()
                .sorted(Comparator.comparing(WorkoutSession::getStartedAt))
                .map(WorkoutSessionResult::from)
                .toList();
    }

    private Routine loadRoutine(String routineName) throws RoutineNotFoundException {
        RoutineName name = new RoutineName(routineName);
        return routineRepository.loadRoutine(name).orElseThrow(() -> new RoutineNotFoundException(name));
    }
}
