package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.repository.WorkoutRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;

import java.util.ArrayList;
import java.util.List;

public class WorkoutService {

    private final RoutineRepository routineRepository;
    private final WorkoutRepository workoutRepository;

    public WorkoutService(RoutineRepository routineRepository, WorkoutRepository workoutRepository) {
        this.routineRepository = routineRepository;
        this.workoutRepository = workoutRepository;
    }

    public void saveWorkout(String routineName, Workout workout) throws RoutineNotFoundException {
        if (workout == null) {
            throw new NullPointerException("Workout cannot be null");
        }

        routineRepository.loadRoutine(new RoutineName(routineName))
                .map(r -> workoutRepository.save(r, workout))
                .orElseThrow(() -> new RoutineNotFoundException(new RoutineName(routineName)));
    }

    public List<Workout> loadWorkoutsForRoutine(String routineName) {
        return new ArrayList<>();
    }
}
