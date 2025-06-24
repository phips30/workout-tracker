package com.phips30.workouttracker.workout.domain.service;


import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.repository.WorkoutRepository;

import java.util.List;

public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public Routine loadRoutineWithWorkouts(String routineName) {
        return workoutRepository.loadRoutine(routineName)
                .map(routine -> {
                    List<Workout> workoutsForRoutine = workoutRepository.loadWorkoutsForRoutine(routine);
                    routine.addWorkouts(workoutsForRoutine);
                    return routine;
                })
                .orElseThrow(() -> new RuntimeException("Routine not found"));
    }
}
