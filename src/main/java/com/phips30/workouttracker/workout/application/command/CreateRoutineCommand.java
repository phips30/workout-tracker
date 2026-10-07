package com.phips30.workouttracker.workout.application.command;

import java.util.List;
import java.util.UUID;

public record CreateRoutineCommand(String name,
                                   String routineType,
                                   List<UUID> exerciseIds,
                                   List<Integer> repetitions) {
}
