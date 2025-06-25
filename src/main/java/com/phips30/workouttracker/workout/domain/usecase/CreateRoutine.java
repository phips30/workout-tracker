package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.entity.Workout;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.repository.WorkoutRepository;
import com.phips30.workouttracker.workout.domain.valueobjects.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import lombok.Builder;
import lombok.Value;

import java.util.List;

public class CreateRoutine {

    private final RoutineRepository routineRepository;

    public CreateRoutine(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    public void execute(InputValues input) throws RoutineAlreadyExistsException {
        Routine routine = Routine.of(
                input.name,
                input.routineType,
                input.exercises.stream().map(Exercise::of).toList(),
                input.repetitions.stream().map(Repetition::of).toList());
        if(routineRepository.exists(routine.getName())) {
            throw new RoutineAlreadyExistsException(routine.getName());
        }

        routineRepository.saveRoutine(routine);
    }

    @Value
    @Builder(builderClassName = "Builder", setterPrefix = "with")
    public static class InputValues {
        String name;
        RoutineType routineType;
        List<String> exercises;
        List<Integer> repetitions;
    }
}
