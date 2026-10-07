package com.phips30.workouttracker.workout.application.usecase;

import com.phips30.workouttracker.workout.application.command.CreateRoutineCommand;
import com.phips30.workouttracker.workout.application.result.RoutineDetailResult;
import com.phips30.workouttracker.workout.application.result.RoutineResult;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.exceptions.ExerciseNotFoundException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.service.RoutineFactory;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;

import java.util.List;

public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineFactory routineFactory;

    public RoutineService(RoutineRepository routineRepository, RoutineFactory routineFactory) {
        this.routineRepository = routineRepository;
        this.routineFactory = routineFactory;
    }

    public void createRoutine(CreateRoutineCommand command)
            throws RoutineAlreadyExistsException, ExerciseNotFoundException {
        Routine routine = routineFactory.of(
                new RoutineName(command.name()),
                RoutineType.valueOf(command.routineType()),
                command.exerciseIds().stream().map(EntityId::new).toList(),
                command.repetitions().stream().map(Repetition::of).toList());

        routineRepository.saveRoutine(routine);
    }

    public RoutineDetailResult loadRoutine(String routineName) throws RoutineNotFoundException {
        RoutineName name = new RoutineName(routineName);
        Routine routine = routineRepository.loadRoutine(name)
                .orElseThrow(() -> new RoutineNotFoundException(name));
        return RoutineDetailResult.from(routine);
    }

    public List<RoutineResult> loadRoutines() {
        return routineRepository.loadRoutines().stream()
                .map(RoutineResult::from)
                .toList();
    }
}
