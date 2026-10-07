package com.phips30.workouttracker.workout.application.usecase;

import com.phips30.workouttracker.workout.application.command.CreateWorkoutCommand;
import com.phips30.workouttracker.workout.application.result.WorkoutResult;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.exceptions.RoutineNotFoundException;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.repository.WorkoutRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.Round;
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

    public void saveWorkout(CreateWorkoutCommand command) throws RoutineNotFoundException {
        RoutineName routineName = new RoutineName(command.routineName());
        Routine routine = routineRepository.loadRoutine(routineName)
                .orElseThrow(() -> new RoutineNotFoundException(routineName));

        Workout workout = Workout.of(
                command.startedAt(),
                command.roundDurations().stream().map(Round::new).toList(),
                command.metadata());

        workoutRepository.save(routine, workout);
    }

    public List<WorkoutResult> loadWorkoutsForRoutine(String routineName) {
        return new ArrayList<>();
    }
}
