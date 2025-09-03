package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.repository.WorkoutRepository;

import java.util.Set;

public class LoadRoutine {

    private final RoutineRepository routineRepository;
    private final WorkoutRepository workoutRepository;

    public LoadRoutine(RoutineRepository routineRepository, WorkoutRepository workoutRepository) {
        this.routineRepository = routineRepository;
        this.workoutRepository = workoutRepository;
    }

    public Routine loadRoutineWithWorkouts(String routineName) throws RoutineNotFoundException {
        return routineRepository.loadRoutine(routineName)
                .map(routine -> {
                    Set<Workout> workoutsForRoutine = workoutRepository.loadWorkoutsForRoutine(routine);
                    routine.addWorkouts(workoutsForRoutine);
                    return routine;
                })
                .orElseThrow(() -> new RoutineNotFoundException(routineName));
    }

}
