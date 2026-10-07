package com.phips30.workouttracker.workout.application.command;

import java.util.List;
import java.util.UUID;

public record CreateRoutineCommand(String name,
                                   String routineType,
                                   List<Block> blocks) {

    public record Block(int position, int rounds, List<Item> items) {
    }

    /**
     * @param repetitionType NUMBER or SECONDS; defaults to NUMBER when null
     * @param repetitions    the number of repetitions or the number of seconds, depending on the type
     */
    public record Item(int position, UUID exerciseId, String repetitionType, int repetitions) {
    }
}
