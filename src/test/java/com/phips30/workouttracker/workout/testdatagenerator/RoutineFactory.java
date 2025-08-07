package com.phips30.workouttracker.workout.testdatagenerator;

import com.phips30.workouttracker.RandomData;
import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import com.phips30.workouttracker.workout.infrastructure.rest.dto.NewRoutineRequest;

import java.util.List;

public class RoutineFactory {

    public static NewRoutineRequest createNewRoutineRequest() {
        return new NewRoutineRequest(
                RandomData.shortString(),
                RoutineType.AMRAP,
                List.of(RandomData.shortString(), RandomData.shortString()),
                List.of(RandomData.positiveDigit(), RandomData.positiveDigit())
        );
    }
}
