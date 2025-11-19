package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineAlreadyExistsException;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;

import java.util.List;

public class RoutineService {
    private final RoutineRepository routineRepository;

    public RoutineService(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    public void createRoutine(String name,
                        RoutineType type,
                        List<String> exercises,
                        List<Integer> repetitions) throws RoutineAlreadyExistsException {
        Routine routine = Routine.createNew(
                new RoutineName(name),
                type,
                exercises.stream().map(e -> new Exercise(new ExerciseName(e))).toList(),
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
