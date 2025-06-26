package com.phips30.workouttracker.workout.infrastructure.csvdb;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
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
