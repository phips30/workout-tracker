package com.phips30.workouttracker.workout.TestDataGenerator;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.application.result.RepetitionResult;
import com.phips30.workouttracker.workout.application.result.RoutineDetailResult;
import com.phips30.workouttracker.workout.application.result.RoutineResult;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;

import java.util.List;

public class RoutineFactory {

    private Routine routine;

    public static NewRoutineRequest createNewRoutineRequest() {
        return createNewRoutineRequest(
                List.of(RandomData.randomUUID().toString(), RandomData.randomUUID().toString()),
                List.of(RandomData.positiveDigit(), RandomData.positiveDigit())
        );
    }

    public static NewRoutineRequest createNewRoutineRequest(List<String> exerciseIds, List<Integer> repetitions) {
        return new NewRoutineRequest(
                RandomData.shortString(),
                RoutineType.AMRAP.name(),
                exerciseIds,
                repetitions
        );
    }

    public static RoutineResult createRoutineResult() {
        return new RoutineResult(RandomData.shortString(), RoutineType.AMRAP.name());
    }

    public static RoutineDetailResult createRoutineDetailResult() {
        return new RoutineDetailResult(
                List.of(ExerciseFactory.createExerciseResult(), ExerciseFactory.createExerciseResult()),
                List.of(new RepetitionResult("NUMBER", RandomData.positiveDigit()),
                        new RepetitionResult("NUMBER", RandomData.positiveDigit()))
        );
    }

    public static RoutineFactory createRoutine() {
        return createRoutine(RandomData.shortString());
    }

    public static RoutineFactory createRoutine(String name) {
        RoutineFactory routineFactory = new RoutineFactory();
        routineFactory.routine = Routine.of(
                EntityId.generate(),
                new RoutineName(name),
                RoutineType.AMRAP,
                List.of(new Exercise(new ExerciseName(RandomData.shortString())), new Exercise(new ExerciseName(RandomData.shortString()))),
                List.of(Repetition.of(RandomData.positiveDigit()), Repetition.of(RandomData.positiveDigit()))
        );
        return routineFactory;
    }

    public Routine build() {
        return this.routine;
    }
}
