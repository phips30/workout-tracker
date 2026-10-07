package com.phips30.workouttracker.workout.TestDataGenerator;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.application.result.RepetitionResult;
import com.phips30.workouttracker.workout.application.result.RoutineBlockItemResult;
import com.phips30.workouttracker.workout.application.result.RoutineBlockResult;
import com.phips30.workouttracker.workout.application.result.RoutineDetailResult;
import com.phips30.workouttracker.workout.application.result.RoutineResult;
import com.phips30.workouttracker.workout.domain.entity.Exercise;
import com.phips30.workouttracker.workout.domain.entity.Routine;
import com.phips30.workouttracker.workout.domain.entity.RoutineBlock;
import com.phips30.workouttracker.workout.domain.entity.RoutineBlockItem;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.domain.valueobjects.EntityId;
import com.phips30.workouttracker.workout.domain.valueobjects.ExerciseName;
import com.phips30.workouttracker.workout.domain.valueobjects.Repetition;
import com.phips30.workouttracker.workout.domain.valueobjects.RepetitionType;
import com.phips30.workouttracker.workout.domain.valueobjects.RoutineName;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.RoutineBlockItemRequest;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.RoutineBlockRequest;

import java.util.List;
import java.util.stream.IntStream;

public class RoutineFactory {

    private Routine routine;

    public static NewRoutineRequest createNewRoutineRequest() {
        return createNewRoutineRequest(
                List.of(RandomData.randomUUID().toString(), RandomData.randomUUID().toString()));
    }

    /**
     * Creates a request with a single block that contains one item per given exercise id
     */
    public static NewRoutineRequest createNewRoutineRequest(List<String> exerciseIds) {
        List<RoutineBlockItemRequest> items = IntStream.range(0, exerciseIds.size())
                .mapToObj(i -> new RoutineBlockItemRequest(
                        i + 1, exerciseIds.get(i), RepetitionType.NUMBER.name(), RandomData.positiveDigit()))
                .toList();
        return new NewRoutineRequest(
                RandomData.shortString(),
                RoutineType.AMRAP.name(),
                List.of(new RoutineBlockRequest(1, RandomData.positiveDigit(), items))
        );
    }

    public static RoutineResult createRoutineResult() {
        return new RoutineResult(RandomData.shortString(), RoutineType.AMRAP.name());
    }

    public static RoutineDetailResult createRoutineDetailResult() {
        return new RoutineDetailResult(List.of(new RoutineBlockResult(
                1,
                RandomData.positiveDigit(),
                List.of(
                        new RoutineBlockItemResult(1, ExerciseFactory.createExerciseResult(),
                                new RepetitionResult("NUMBER", RandomData.positiveDigit())),
                        new RoutineBlockItemResult(2, ExerciseFactory.createExerciseResult(),
                                new RepetitionResult("SECONDS", RandomData.positiveDigit()))))));
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
                List.of(RoutineBlock.of(1, RandomData.positiveDigit(), List.of(
                        RoutineBlockItem.of(1, new Exercise(new ExerciseName(RandomData.shortString())),
                                Repetition.of(RandomData.positiveDigit())),
                        RoutineBlockItem.of(2, new Exercise(new ExerciseName(RandomData.shortString())),
                                Repetition.of(RepetitionType.SECONDS, RandomData.positiveDigit())))))
        );
        return routineFactory;
    }

    public Routine build() {
        return this.routine;
    }
}
