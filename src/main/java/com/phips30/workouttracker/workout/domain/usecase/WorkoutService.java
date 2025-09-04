package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkoutService {

    private final RoutineRepository routineRepository;

    public WorkoutService(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    public void saveWorkout(String routineName, Workout workout) {
        if (workout == null) {
            throw new NullPointerException("Workout cannot be null");
        }
    }

    public List<Workout> loadWorkoutsForRoutine(String routineName) {
        return new ArrayList<>();
    }
}
