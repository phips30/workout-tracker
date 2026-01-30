package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;

import java.util.List;
import java.util.UUID;

public class RoutineService {
    private final RoutineRepository routineRepository;
    private final ExerciseService exerciseService;

    public RoutineService(RoutineRepository routineRepository, ExerciseService exerciseService) {
        this.routineRepository = routineRepository;
        this.exerciseService = exerciseService;
    }

    public void createRoutine(String name,
                        RoutineType type,
                        List<UUID> exerciseIds,
                        List<Integer> repetitions) throws RoutineAlreadyExistsException {
        List<Exercise> exercises = exerciseService.loadByIds(exerciseIds);

        Routine routine = Routine.createNew(
                new RoutineName(name),
                type,
                exercises,
                repetitions.stream().map(Repetition::of).toList());
        if (routineRepository.exists(routine.getName())) {
            throw new RoutineAlreadyExistsException(routine.getName());
        }

        routineRepository.saveRoutine(routine);
    }

    public Routine loadRoutine(RoutineName routineName) throws RoutineNotFoundException {
        return routineRepository.loadRoutine(routineName)
                .orElseThrow(() -> new RoutineNotFoundException(routineName));
    }

    public List<Routine> loadRoutines() {
        return routineRepository.loadRoutines();
    }
}
