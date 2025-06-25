package com.phips30.workouttracker.workout.csvdb;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;

import java.util.Optional;

public class RoutineRepositoryImpl implements RoutineRepository {
    @Override
    public Optional<Routine> loadRoutine(String name) {
        return Optional.empty();
    }

    @Override
    public boolean exists(String name) {
        return false;
    }

    @Override
    public void saveRoutine(Routine routine) {

    }
}
