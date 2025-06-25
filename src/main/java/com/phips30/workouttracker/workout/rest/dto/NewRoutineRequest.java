package com.phips30.workouttracker.workout.rest.dto;

import com.phips30.workouttracker.workout.domain.entity.RoutineType;
import lombok.Value;

import java.util.List;

@Value
public class NewRoutineRequest {
    String name;
    RoutineType routineType;
    List<String> exercises;
    List<Integer> repetitions;
}
