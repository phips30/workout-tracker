package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;

import java.util.List;

public class LoadRoutine {

    private final RoutineRepository routineRepository;

    public LoadRoutine(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    public Routine loadRoutine(String routineName) throws RoutineNotFoundException {
        return routineRepository.loadRoutine(routineName)
                .orElseThrow(() -> new RoutineNotFoundException(routineName));
    }

    public List<Routine> loadRoutines() {
        return routineRepository.loadRoutines();
    }
}
