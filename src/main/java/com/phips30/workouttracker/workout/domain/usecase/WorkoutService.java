package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.workout.domain.entity.Workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutService {

    public Workout saveWorkout(String routineName, Workout workout) {
        return workout;
    }

    public List<Workout> loadWorkoutsForRoutine(String routineName) {
        return new ArrayList<>();
    }
}
