package com.phips30.workouttracker.workout.domain.usecase;

import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.repository.RoutineRepository;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import lombok.Builder;
import lombok.Value;

import java.util.List;

public class CreateRoutine {

    private final RoutineRepository routineRepository;

    public CreateRoutine(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    public void execute(String name,
                        RoutineType type,
                        List<String> exercises,
                        List<Integer> repetitions) throws RoutineAlreadyExistsException {
        Routine routine = Routine.of(
                EntityId.generate(),
                name,
                type,
                exercises.stream().map(Exercise::of).toList(),
                repetitions.stream().map(Repetition::of).toList());
        if(routineRepository.exists(routine.getName())) {
            throw new RoutineAlreadyExistsException(routine.getName().getValue());
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
